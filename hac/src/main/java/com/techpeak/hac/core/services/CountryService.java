package com.techpeak.hac.core.services;

import com.techpeak.hac.core.dtos.CountryResponse;
import com.techpeak.hac.core.models.Country;

import java.util.List;

public interface CountryService {
    List<CountryResponse> list ();
    List<CountryResponse> search(String country);

    Country getCountryOrThrow(Long id);
}
