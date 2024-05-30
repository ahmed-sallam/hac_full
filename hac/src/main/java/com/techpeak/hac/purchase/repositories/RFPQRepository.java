package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.RFPQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RFPQRepository extends JpaRepository<RFPQ, Long> {
    Optional<RFPQ> findTopByOrderByNumberDesc();

}
