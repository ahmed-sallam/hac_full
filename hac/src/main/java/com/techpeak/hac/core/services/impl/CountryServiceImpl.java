package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.dtos.CountryResponse;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.mappers.CountryMapper;
import com.techpeak.hac.core.models.Country;
import com.techpeak.hac.core.repositories.CountryRepository;
import com.techpeak.hac.core.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<CountryResponse> list() {
        List<Country> all = countryRepository.findAll();
        return all.stream().map(CountryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CountryResponse> search(String country) {
        List<Country> all = countryRepository.findAllByNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(country, country);
        return all.stream().map(CountryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Country getCountryOrThrow(Long id) {
        return countryRepository.findById(id).orElseThrow(() -> new NotFoundException("Country with id " + id + " does not exist"));
    }
}
