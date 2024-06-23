package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.SupplierQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SupplierQuotationRepository extends JpaRepository<SupplierQuotation, Long>, JpaSpecificationExecutor<SupplierQuotation> {
    @Query("select sq , uh from SupplierQuotation sq left join fetch sq.lines left join UserHistory  uh on (uh.recordId = sq.id and uh.tableName = 'supplier_quotations')  where sq.id = :id")
    List<Object[]> findByIdWithLines(@Param("id") Long id);


    @Query(value = "SELECT s.id, s.name_ar, s.name_en , COALESCE( JSON_AGG(" +
            "DISTINCT JSONB_BUILD_OBJECT('id', sql.id, " +
            "'price',sql.price, " +
            "'discount', sql.discount, " +
            "'product' , JSONB_BUILD_OBJECT('id', p.id, " +
            "'productNumber', p.product_number, " +
            "'descriptionAr', p.description_ar, " +
            "'descriptionEn', p.description_en, " +
            "'mainBrandAr', mb.name_ar, " +
            "'mainBrandEn', mb.name_en, " +
            "'subBrandAr', sb.name_en, " +
            "'subBrandEn', sb.name_en " +
            " ), " +
            "'supplierQuotation', JSONB_BUILD_OBJECT('id', sq.id, " +
            "'date', sq.date, " +
            "'currency', JSONB_BUILD_OBJECT('id', c.id, " +
            "'name', c.name, " +
            "'code', c.code, " +
            "'exchangeRate', c.exchange_rate" +
            "), " +
            "'isLocal', sq.is_local, " +
            "'paymentTerms', sq.payment_terms" +
            ")  " +
            ")), '[]') " +
            " FROM supplier_quotation_lines  sql " +
            "LEFT JOIN products p ON sql.product_id = p.id  " +
            "LEFT JOIN supplier_quotations sq  ON  sq.id = sql.supplier_quotation_id " +
            "LEFT JOIN public.suppliers s ON s.id = sq.supplier_id " +
            "LEFT JOIN public.brands mb on p.main_brand_id = mb.id  " +
            "LEFT JOIN public.brands sb on p.sub_brand_id = sb.id  " +
            "LEFT JOIN public.currencies c on c.id = sq.currency_id  " +
            "WHERE p.product_number = ?1 AND sq.date >= ?2 " +
            "GROUP BY s.id , sq.date ORDER BY sq.date DESC ", nativeQuery = true )
    List<Object[]> getSupplierQuotationsGroupBySupplier(@Param("productNumber") String productNumber, @Param("fromDate") LocalDate fromDate);

}
