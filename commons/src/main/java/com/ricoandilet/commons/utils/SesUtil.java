/*
  Copyright (C) 2018-2021 YouYu information technology (Shanghai) Co., Ltd.
  <p>
  All right reserved.
  <p>
  This software is the confidential and proprietary
  information of YouYu Company of China.
  ("Confidential Information"). You shall not disclose
  such Confidential Information and shall use it only
  in accordance with the terms of the contract agreement
  you entered into with YouYu inc.
 */
package com.ricoandilet.commons.utils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.xbill.DNS.Record;
import org.xbill.DNS.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: rico
 * @date: 2024/1/6
 **/
public class SesUtil {

    public static final SesClient sesClient = SesClient.builder()
            .region(Region.US_WEST_1)
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("ACCESS_KEY","SECRET_KEY")))
            .build();

    public static List<String> addDomain(String domain){
        VerifyDomainDkimResponse verifyDomainDkimResponse = sesClient.verifyDomainDkim(builder -> {
            builder.domain(domain).build();
        });

        List<String> tokens =  verifyDomainDkimResponse.dkimTokens();
        return tokens;
    }

    public static List<String> getDkimTokensByDomain(String domain){
        GetIdentityDkimAttributesResponse getIdentityDkimAttributesResponse = sesClient.getIdentityDkimAttributes(builder -> {
            builder.identities(domain);
        });
        IdentityDkimAttributes dkimAttributes = getIdentityDkimAttributesResponse.dkimAttributes().get(domain);
        return dkimAttributes.dkimTokens();
    }

    public static VerificationStatus getDomainStatus(String domain){
        GetIdentityVerificationAttributesResponse getIdentityVerificationAttributesResponse = sesClient.getIdentityVerificationAttributes(builder -> {
            builder.identities(domain);
        });

        IdentityVerificationAttributes identityVerificationAttributes = getIdentityVerificationAttributesResponse.verificationAttributes().get(domain);
        return identityVerificationAttributes.verificationStatus();
    }

    public static void deleteIdentity(String domain){
        DeleteIdentityResponse deleteIdentityResponse = sesClient.deleteIdentity(builder -> {
            builder.identity(domain);
        });
    }
}

