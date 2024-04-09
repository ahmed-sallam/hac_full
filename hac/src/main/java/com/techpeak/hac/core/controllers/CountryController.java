package com.techpeak.hac.core.controllers;

import com.techpeak.hac.core.dtos.CountryResponse;
import com.techpeak.hac.core.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;
    @GetMapping
    public ResponseEntity<List<CountryResponse>> list(@RequestParam(name = "country", defaultValue = "") String country) {
        if (country.isEmpty()) {
            return ResponseEntity.ok(countryService.list());
        } else {
            return ResponseEntity.ok(countryService.search(country));
        }
    }
}
