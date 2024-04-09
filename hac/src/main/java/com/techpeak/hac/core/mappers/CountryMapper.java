package com.techpeak.hac.core.mappers;

import com.techpeak.hac.core.dtos.CountryResponse;
import com.techpeak.hac.core.models.Country;

public class CountryMapper {
    public static CountryResponse toDto(Country country) {
        return new CountryResponse(country.getId(), country.getNameAr(), country.getNameEn());
    }
}
