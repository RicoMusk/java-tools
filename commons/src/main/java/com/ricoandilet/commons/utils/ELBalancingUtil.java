package com.ricoandilet.commons.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingClientBuilder;
import com.amazonaws.services.elasticloadbalancingv2.model.*;

/**
 * @author: rico
 * @date: 2023/8/25
 * Elastic Load Balancing
 **/
public class ELBalancingUtil {

   private final static String accessKey = "accessKey";
   private final static String secretKey = "secretKey";


   public static void allLoadBalancers() {

      DescribeLoadBalancersRequest describeLoadBalancersRequest = new DescribeLoadBalancersRequest();
      DescribeLoadBalancersResult describeLoadBalancersResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().describeLoadBalancers(describeLoadBalancersRequest);

      System.out.println(describeLoadBalancersResult);
   }

   public static void allListeners() {

      DescribeListenersRequest describeListenersRequest = new DescribeListenersRequest();
      describeListenersRequest.withLoadBalancerArn("arn:aws:elasticloadbalancing:us-west-1:348152033681:loadbalancer/app/dev-app-alb/61fb0f1d5f89bea8");
      // describeListenersRequest.withListenerArns("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      DescribeListenersResult describeListenersResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().describeListeners(describeListenersRequest);

      System.out.println(describeListenersResult);
   }

   public static void allCertificates() {

      DescribeListenerCertificatesRequest describeListenerCertificatesRequest = new DescribeListenerCertificatesRequest();
      describeListenerCertificatesRequest.withListenerArn("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      // describeListenersRequest.withListenerArns("arn:aws:elasticloadbalancing:us-west-1:348152033681:listener/app/dev-app-alb/61fb0f1d5f89bea8/03668b5e62a0db9f");
      DescribeListenerCertificatesResult describeListenersResult = AmazonElasticLoadBalancingClientBuilder.standard()
              .withRegion(Regions.US_WEST_1)
              .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
              .build().describeListenerCertificates(describeListenerCertificatesRequest);

      System.out.println(describeListenersResult);
   }

   public static void addCertificates() {

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
