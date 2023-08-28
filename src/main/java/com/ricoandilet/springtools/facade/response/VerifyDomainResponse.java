package com.ricoandilet.springtools.facade.response;

import com.ricoandilet.springtools.model.enums.DomainTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyDomainResponse {

    private String domainName;
    private Record verifyRecord;
    @Data
    @Builder
    public static class Record{
        private DomainTypeEnum domainType;
        private String recordName;
        private String recordValue;
    }
}
