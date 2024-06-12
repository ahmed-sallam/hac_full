package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.SupplierQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierQuotationRepository extends JpaRepository<SupplierQuotation, Long>, JpaSpecificationExecutor<SupplierQuotation> {
    @Query("select sq , uh from SupplierQuotation sq left join fetch sq.lines left join UserHistory  uh on (uh.recordId = sq.id and uh.tableName = 'supplier_quotations')  where sq.id = :id")
    List<Object[]> findByIdWithLines(@Param("id") Long id);

}
