package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.SupplierQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierQuotationRepository extends JpaRepository<SupplierQuotation, Long>, JpaSpecificationExecutor<SupplierQuotation> {
    @Query("select sq from SupplierQuotation sq join fetch sq.lines where sq.id = :id")
    Optional<SupplierQuotation> findByIdLines(@Param("id") Long id);

}
