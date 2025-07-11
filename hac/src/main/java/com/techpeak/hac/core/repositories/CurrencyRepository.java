package com.techpeak.hac.core.repositories;

import com.techpeak.hac.core.models.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByCode(String code);

    Optional<CurrencyEntity> findByName(String name);
}
