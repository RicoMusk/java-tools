package com.ricoandilet.springtools.facade.response;

import com.ricoandilet.springtools.model.enums.DomainTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DomainResponse {

    private String domainName;
    private List<Record> records;
    @Data
    @Builder
    public static class Record{
        private DomainTypeEnum domainType;
        private String recordName;
        private String recordValue;
    }
}
