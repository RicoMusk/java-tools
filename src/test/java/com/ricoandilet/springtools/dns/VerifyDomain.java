package com.ricoandilet.springtools.dns;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

/**
 * @author: rico
 * @date: 2023/8/18
 **/
public class VerifyDomain {

   @Disabled
   @Test
   void verifyCNAMERecord() {

      String domain = "t3d7yaaxtdxklery2sv37vaayipxyksl._domainkey.youland.com";
      String cnameVerificationDomain = "t3d7yaaxtdxklery2sv37vaayipxyksl.dkim.amazonses.com";

      try {
         Lookup lookup = new Lookup(domain, Type.CNAME);
         Record[] records = lookup.run();

         if (records != null) {
            for (Record record : records) {
               if (record instanceof CNAMERecord) {
                  CNAMERecord cnameRecord = (CNAMERecord) record;
                  if (cnameRecord.getTarget().toString().equalsIgnoreCase(cnameVerificationDomain)) {
                     Assertions.assertTrue(true, "CNAME Record verification successful.");
                     return;
                  }
               }
            }
         }
         Assertions.assertFalse(false, "CNAME Record verification failed.");
      } catch (TextParseException e) {
         e.printStackTrace();
      }
   }

   @Disabled
   @Test
   void verifyTXTRecord() {

      String domain = "_amazonses.youland.com";
      String verificationCode = "7oqJgVCkzx0KmsyDG8Gfks6xSpN+BslLogSOxhI9XHE=";
      try {

         Lookup lookup = new Lookup(domain, Type.TXT);
         Record[] records = lookup.run();

         if (records != null) {
            for (Record record : records) {
               if (record instanceof TXTRecord) {
                  TXTRecord txtRecord = (TXTRecord) record;
                  if (txtRecord.getStrings().contains(verificationCode)) {
                     Assertions.assertTrue(true, "TXT Record verification successful.");
                     return;
                  }
               }
            }
         }
         Assertions.fail("TXT Record verification failed.");
      } catch (TextParseException e) {
         e.printStackTrace();
      }
   }
}
