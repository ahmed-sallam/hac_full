package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.BidSummaryLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidSummaryLineRepository extends JpaRepository<BidSummaryLine, Long> {
}
