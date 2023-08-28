package com.ricoandilet.springtools.awselb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingClientBuilder;
import com.amazonaws.services.elasticloadbalancingv2.model.*;
import org.junit.jupiter.api.Test;

/**
 * @author: rico
 * @date: 2023/8/18
 **/
public class AWSElb {

   private final String accessKey = "accessKey";
   private final String secretKey = "secretKey";


   @Test
   void allLoadBalancers() {

      DescribeLoadBalancersRequest describeLoadBalancersRequest = new DescribeLoadBalancersRequest();
      DescribeLoadBalancersResult describeLoadBalancersResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().describeLoadBalancers(describeLoadBalancersRequest);

      System.out.println(describeLoadBalancersResult);
   }

   @Test
   void allListeners() {

      DescribeListenersRequest describeListenersRequest = new DescribeListenersRequest();
      describeListenersRequest.withLoadBalancerArn("arn:aws:elasticloadbalancing:us-west-1:348152033681:loadbalancer/app/dev-app-alb/61fb0f1d5f89bea8");
      // describeListenersRequest.withListenerArns("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      DescribeListenersResult describeListenersResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().describeListeners(describeListenersRequest);

      System.out.println(describeListenersResult);
   }

   @Test
   void allCertificates() {

      DescribeListenerCertificatesRequest describeListenerCertificatesRequest = new DescribeListenerCertificatesRequest();
      describeListenerCertificatesRequest.withListenerArn("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      // describeListenersRequest.withListenerArns("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      DescribeListenerCertificatesResult describeListenersResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().describeListenerCertificates(describeListenerCertificatesRequest);

      System.out.println(describeListenersResult);
   }

   @Test
   void addCertificates() {

      AddListenerCertificatesRequest addListenerCertificatesRequest = new AddListenerCertificatesRequest();
      Certificate certificate = new Certificate();
      certificate.withCertificateArn("arn:aws:acm:us-west-1:348152033681:certificate/fba188bc-6310-458e-8a89-b10446acf394");
      addListenerCertificatesRequest.withCertificates(certificate);
      addListenerCertificatesRequest.withListenerArn("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      AddListenerCertificatesResult addListenerCertificatesResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().addListenerCertificates(addListenerCertificatesRequest);

      System.out.println(addListenerCertificatesResult);
   }
}
