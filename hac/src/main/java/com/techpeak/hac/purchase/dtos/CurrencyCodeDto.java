package com.techpeak.hac.purchase.dtos;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.core.models.CurrencyEntity}
 */
@Value
public class CurrencyCodeDto implements Serializable {
    Long id;
    String code;
}