package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.purchase.dtos.CurrencyCodeDto;

public class CurrencyMapper {
    private CurrencyMapper() {
    }
    public static CurrencyCodeDto mapToCurrencyCodeDto(CurrencyEntity currencyEntity) {
        return CurrencyCodeDto.builder()
                .id(currencyEntity.getId())
                .code(currencyEntity.getCode())
                .build();
    }
}
