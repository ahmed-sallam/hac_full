package com.techpeak.hac.core.repositories;

import com.techpeak.hac.core.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findAllByNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(String country, String country2);
}
