package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.SupplierQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierQuotationRepository extends JpaRepository<SupplierQuotation, Long>, JpaSpecificationExecutor<SupplierQuotation> {
}
