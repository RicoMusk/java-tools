package com.ricoandilet.springtools.facade.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DomainRequest{

    private String domainName;

}
