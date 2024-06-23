package com.techpeak.hac.purchase.dtos;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.core.models.CurrencyEntity}
 */
public record CurrencyCodeResponse(Long id, String name, String code,
                                   float exchangeRate) implements Serializable {
}