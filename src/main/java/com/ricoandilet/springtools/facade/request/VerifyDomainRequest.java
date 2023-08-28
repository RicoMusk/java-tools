package com.ricoandilet.springtools.facade.request;

import com.ricoandilet.springtools.model.enums.DomainTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VerifyDomainRequest{

    private String domainName;
    private Record record;
    @Data
    @Builder
    public static class Record{
        private DomainTypeEnum domainType;
        private String recordName;
        private String recordValue;
    }

}
