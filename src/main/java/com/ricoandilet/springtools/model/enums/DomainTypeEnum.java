/**
 * * Youland.com copyright
 */
package com.ricoandilet.springtools.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DomainTypeEnum {

    A("A"),

    CNAME("CNAME"),

    TXT("TXT");

    private final String type;
}
