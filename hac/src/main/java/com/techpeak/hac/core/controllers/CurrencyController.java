package com.techpeak.hac.core.controllers;

import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyEntity>> list(){
        List<CurrencyEntity> all =  currencyService.getAllCurrencies();
        return ResponseEntity.ok(all);
    }
}
