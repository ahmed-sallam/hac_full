package com.techpeak.hac.purchase.dtos;

import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.core.models.CurrencyEntity}
 */
@Builder
public record CurrencyEntityDto(Long id, String name, String code,
                                float exchangeRate) implements Serializable {
}