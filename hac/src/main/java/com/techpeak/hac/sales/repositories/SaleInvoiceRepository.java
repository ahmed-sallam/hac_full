package com.techpeak.hac.sales.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpeak.hac.sales.models.SaleInvoice;

public interface SaleInvoiceRepository extends JpaRepository<SaleInvoice, Long>, JpaSpecificationExecutor<SaleInvoice> {

    Optional<SaleInvoice> findTopByOrderByNumberDesc();

    @Query("select si, uh from SaleInvoice si left join fetch si.lines left join " +
            "UserHistory uh on (uh.recordId = si.id and uh.tableName = 'sale_invoices') where si.id = :id")
    List<Object[]> findByIdWithLines(@Param("id") Long id);
}