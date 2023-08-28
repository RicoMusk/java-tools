package com.ricoandilet.commons.utils;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

/**
 * @author: rico
 * @date: 2023/8/18
 **/
public class DomainVerificationUtil {

    /**
     *
     * @param domain yourDomain.com, sub.yourDomain.com
     * @param data verificationData
     * @return
     */
    public static boolean verifyCNAMERecord(String domain, String data){
        try {
            Lookup lookup = new Lookup(domain, Type.CNAME);
            Record[] records = lookup.run();

            if (records != null) {
                for (Record record : records) {
                    if (record instanceof CNAMERecord) {
                        CNAMERecord cnameRecord = (CNAMERecord) record;
                        if (cnameRecord.getTarget().toString().equalsIgnoreCase(data)) {
                            return true;
                        }
                    }
                }
            }
        } catch (TextParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param domain yourDomain.com, sub.yourDomain.com
     * @param data verificationData
     * @return
     */
    public static boolean verifyTXTRecord(String domain, String data){

        try {
            Lookup lookup = new Lookup(domain, Type.TXT);
            Record[] records = lookup.run();

            if (records != null) {
                for (Record record : records) {
                    if (record instanceof TXTRecord) {
                        TXTRecord txtRecord = (TXTRecord) record;
                        if (txtRecord.getStrings().contains(data)) {
                            return true;
                        }
                    }
                }
            }
        } catch (TextParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
