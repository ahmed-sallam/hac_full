package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.repositories.CurrencyRepository;
import com.techpeak.hac.core.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    private static String getMessage(String title, Object id) {
        return "Currency with " + title+ " " + id + " not found";
    }

    @Override
    public CurrencyEntity getCurrencyById(Long id) {

        return getOrElseThrow(id);
    }

    private CurrencyEntity getOrElseThrow(Long id) {
        return currencyRepository.findById(id).orElseThrow(() -> new NotFoundException(getMessage( "id" , id)));
    }

    @Override
    public CurrencyEntity getCurrencyByCode(String code) {

        return currencyRepository.findByCode(code).orElseThrow(() -> new NotFoundException(getMessage("code",code)));
    }

    @Override
    public CurrencyEntity getCurrencyByName(String name) {
         return currencyRepository.findByName(name).orElseThrow(() -> new NotFoundException(getMessage("name",name)));
    }

    @Override
    public CurrencyEntity saveCurrency(CurrencyEntity currencyEntity) {
         return currencyRepository.save(currencyEntity);
    }

    @Override
    public CurrencyEntity updateCurrency(Long id , CurrencyEntity currencyEntity) {
        CurrencyEntity currency = getOrElseThrow(id);
        currency.setExchangeRate(currencyEntity.getExchangeRate());
        return currencyRepository.save(currency);
    }

    @Override
    public List<CurrencyEntity> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}
