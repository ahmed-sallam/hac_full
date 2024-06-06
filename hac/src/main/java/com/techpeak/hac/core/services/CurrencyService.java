package com.techpeak.hac.core.services;

import com.techpeak.hac.core.models.CurrencyEntity;

import java.util.List;

public interface CurrencyService {
    CurrencyEntity getCurrencyById(Long id);
    CurrencyEntity getCurrencyByCode(String code);
    CurrencyEntity getCurrencyByName(String name);
    CurrencyEntity saveCurrency(CurrencyEntity currencyEntity);
    CurrencyEntity updateCurrency(Long id , CurrencyEntity currencyEntity);
    List<CurrencyEntity> getAllCurrencies();

}
