package com.youland.commons.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) {
        try {
            BigDecimal value = p.getDecimalValue();
            if (value == null || BigDecimal.ZERO.equals(value)) {
                return value;
            }
            return value.setScale(2, RoundingMode.CEILING);
        } catch (Exception e) {
            log.error("Json parse error", e);
        }
        return null;
    }
}
