package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.BidSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BidSummaryRepository extends JpaRepository<BidSummary, Long>, JpaSpecificationExecutor<BidSummary> {
    Optional<BidSummary> findTopByOrderByNumberDesc();
}
