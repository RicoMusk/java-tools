package com.ricoandilet.springtools.facade.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.certificatemanager.AWSCertificateManager;
import com.amazonaws.services.certificatemanager.AWSCertificateManagerClientBuilder;
import com.amazonaws.services.certificatemanager.model.*;
import com.ricoandilet.springtools.exception.BaseException;
import com.ricoandilet.springtools.facade.DomainFacade;
import com.ricoandilet.springtools.facade.request.DomainRequest;
import com.ricoandilet.springtools.facade.request.VerifyDomainRequest;
import com.ricoandilet.springtools.facade.response.DomainResponse;
import com.ricoandilet.springtools.facade.response.VerifyDomainResponse;
import com.ricoandilet.springtools.json.JsonUtil;
import com.ricoandilet.springtools.model.enums.DomainTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Record;
import org.xbill.DNS.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author: rico
 * @date: 2023/8/24
 **/
@Service
@Slf4j
public class DomainFacadeImpl implements DomainFacade {


    private final String accessKey = "accessKey";
    private final String secretKey = "secretKey";

    @Value("spring.profiles.active")
    private String active;

    @Override
    public VerifyDomainResponse getDomain(DomainRequest domainRequest) {

        // Create a client.
        AWSCertificateManager client = AWSCertificateManagerClientBuilder.standard()
                .withRegion(Regions.US_WEST_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
        // Specify a SAN.
        ArrayList<String> san = new ArrayList<String>();
        san.add("*.".concat(domainRequest.getDomainName()));
        san.add(domainRequest.getDomainName());
        // Create a request object and set the input parameters.
        RequestCertificateRequest req = new RequestCertificateRequest();
        req.withDomainName(domainRequest.getDomainName());
        req.setIdempotencyToken("1Aq25pTy");
        req.withValidationMethod(ValidationMethod.DNS);
        req.withGeneralProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                log.info("GetDomain.progressChanged", JsonUtil.stringify(progressEvent));
            }
        });
        req.withSubjectAlternativeNames(san);
        // Create a result object and display the certificate ARN.
        RequestCertificateResult result = client.requestCertificate(req);
        // Display the ARN.
        String arn = result.getCertificateArn();

        // Get
        DescribeCertificateRequest describeCertificateRequest = new DescribeCertificateRequest();
        DescribeCertificateResult describeCertificateResult = client.describeCertificate(describeCertificateRequest.withCertificateArn(arn));
        DomainValidation domainValidation = describeCertificateResult.getCertificate().getDomainValidationOptions().get(0);
        domainValidation.getValidationStatus();
        ResourceRecord resourceRecord = domainValidation.getResourceRecord();
        VerifyDomainResponse domainResponse = VerifyDomainResponse.builder()
                .domainName(domainRequest.getDomainName())
                .verifyRecord(VerifyDomainResponse.Record.builder()
                        .domainType(DomainTypeEnum.CNAME)
                        .recordName(resourceRecord.getName())
                        .recordValue(resourceRecord.getValue())
                        .build())
                .build();

        return domainResponse;
    }

    @Override
    public DomainResponse verifyDomain(VerifyDomainRequest verifyDomainRequest) {

        try {
            VerifyDomainRequest.Record  verifyRecord =  verifyDomainRequest.getRecord();
            String cname = verifyRecord.getRecordName();
            String cnameValue = verifyRecord.getRecordValue();

            Lookup lookup = new Lookup(cname, Type.CNAME);
            Record[] records = lookup.run();
            if (records != null) {
                String[] staticIPs = envStaticIps.get(active);
                Random random = new Random();
                int index = random.nextInt(2);

                for (Record record : records) {
                    if (record instanceof CNAMERecord) {
                        CNAMERecord cnameRecord = (CNAMERecord) record;
                        if (cnameRecord.getTarget().toString().equalsIgnoreCase(cnameValue)) {
                            return DomainResponse.builder()
                                    .domainName(verifyDomainRequest.getDomainName())
                                    .records(List.of(DomainResponse.Record.builder()
                                                    .domainType(DomainTypeEnum.A)
                                                    .recordName("@")
                                                    .recordValue(staticIPs[index])
                                                    .build(),
                                                    DomainResponse.Record.builder()
                                                            .domainType(DomainTypeEnum.A)
                                                            .recordName("www")
                                                            .recordValue(staticIPs[index])
                                                            .build())
                                            )
                                    .build();
                        }
                    }
                }
            }
        } catch (TextParseException e) {
            e.printStackTrace();
        }
        throw new BaseException("Failed", "Validation failed. Add the necessary CNAME records to your DNS");
    }


    private final String[] devStaticIPs = {"15.xxx.225.166", "3.xx.182.183"};
    private final String[] testStaticIPs = {"52.xxx.57.166", "35.xx.149.185"};
    private final String[] prodStaticIPs = {"15.xxx.189.136", "3.xx.174.163"};
    private final Map<String, String[]> envStaticIps = Map.of("dev", devStaticIPs, "test", testStaticIPs, "prod", prodStaticIPs);
}
