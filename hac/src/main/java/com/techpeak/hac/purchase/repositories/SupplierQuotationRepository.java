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
            " FROM products p   " +
            "LEFT JOIN  supplier_quotation_lines  sql ON sql.product_id = p.id  " +
            "LEFT JOIN  supplier_quotations sq  ON  sq.id = sql.supplier_quotation_id " +
            "LEFT JOIN public.suppliers s ON s.id = sq.supplier_id " +
            "LEFT JOIN public.brands mb on p.main_brand_id = mb.id  " +
            "LEFT JOIN public.brands sb on p.sub_brand_id = sb.id  " +
            "LEFT JOIN public.currencies c on c.id = sq.currency_id  " +
            "WHERE p.product_number = ?1 AND sq.date >= ?2 " +
            "GROUP BY s.id  " +
            " ", nativeQuery = true)
    List<Object[]> getSupplierQuotationsGroupBySupplier(@Param("productNumber") String productNumber, @Param("fromDate") LocalDate fromDate);


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
            " FROM products p   " +
            "LEFT JOIN  supplier_quotation_lines  sql ON sql.product_id = p.id  " +
            "LEFT JOIN  supplier_quotations sq  ON  sq.id = sql.supplier_quotation_id " +
            "LEFT JOIN public.suppliers s ON s.id = sq.supplier_id " +
            "LEFT JOIN public.brands mb on p.main_brand_id = mb.id  " +
            "LEFT JOIN public.brands sb on p.sub_brand_id = sb.id  " +
            "LEFT JOIN public.currencies c on c.id = sq.currency_id  " +
            "WHERE p.product_number IN ?1 AND sq.date >= ?2 " +
            "GROUP BY s.id  " +
            " ", nativeQuery = true)
    List<Object[]> getSupplierQuotationsGroupBySupplier(@Param("productNumber") List<String> productNumber, @Param("fromDate") LocalDate fromDate);

    @Query(value = """
                        
            SELECT JSON_BUILD_OBJECT(
                                               'products',
                                               (SELECT COALESCE(JSON_AGG(
                                                                        DISTINCT JSONB_BUILD_OBJECT(
                                                               'id', p.id,
                                                               'productNumber',
                                                               p.product_number,
                                                               'subBrandAr', sb.name_ar,
                                                               'subBrandEn', sb.name_en,
                                                               'quantity', rl.quantity,
                                                               'soldQuantity', COALESCE(sq.sold_quantity, '0')
                                                                                 )
                                                                ), '[]') AS products
                                                FROM products p
                                                         inner Join brands sb ON p.sub_brand_id = sb.id
                                                         inner JOIN rfpq_lines rl
                                                                    ON rl.product_id = p.id
                                
                                                         LEFT JOIN (SELECT it.product_id,
                                                                           SUM(CASE
                                                                                   WHEN transaction_type = 'SALE'
                                                                                       THEN it.quantity
                                                                                   WHEN transaction_type = 'SALE_RETURN'
                                                                                       THEN -it.quantity
                                                                                   ELSE 0 END
                                                                           ) as sold_quantity
                                                                    FROM inventory_transactions it
                                                                             INNER JOIN rfpqs r
                                                                                        ON r.id = :specific_rfpq_id
                                                                    WHERE it.transaction_type IN ('SALE', 'SALE_RETURN')
                                                                      AND (it.transaction_date >=
                                                                           :from_date
                                                                        AND it.transaction_date <= :to_date)
                                                                    GROUP BY it.product_id) sq
                                                                   ON sq.product_id = p.id
                                                WHERE rl.rfpq_id = :specific_rfpq_id),
                                               'suppliers',
                                               (SELECT COALESCE(JSON_AGG(
                                                                        JSON_BUILD_OBJECT(
                                                                                'id', s.id,
                                                                                'nameAr', s.name_ar,
                                                                                'nameEn', s.name_en
                                                                        )
                                                                ), '[]') AS suppliers
                                                FROM suppliers s
                                                WHERE s.id in (SELECT q.supplier_id
                                                               FROM supplier_quotations q
                                                                        JOIN supplier_quotation_lines ql
                                                                             ON ql.supplier_quotation_id = q.id
                                                               WHERE ql.product_id IN (SELECT rl.product_id
                                                                                       FROM rfpq_lines rl
                                                                                       WHERE rl.rfpq_id = :specific_rfpq_id)
                                                                 AND q.date >=
                                                                     :from_date
                                                                 AND q.date <= :to_date)),
                                               'quotations',
                                               (SELECT COALESCE(JSON_AGG(
                                                                        JSON_BUILD_OBJECT(
                                                                                concat(s.id, '/', p.product_number),
                                                                                (SELECT JSON_BUILD_OBJECT(
                                                                                                'id',
                                                                                                q.id,
                                                                                                'date',
                                                                                                q.date,
                                                                                                'currency',
                                                                                                JSON_BUILD_OBJECT(
                                                                                                        'id',
                                                                                                        c.id,
                                                                                                        'name',
                                                                                                        c.name,
                                                                                                        'code',
                                                                                                        c.code,
                                                                                                        'exchangeRate',
                                                                                                        c.exchange_rate
                                                                                                ),
                                                                                                'isLocal',
                                                                                                q.is_local,
                                                                                                'paymentTerms',
                                                                                                q.payment_terms,
                                --                                                             'internalRef', q.internal_ref,
                                                                                                'rfpqId',
                                                                                                q.rfpq_id,
                                                                                                'netPrice',
                                                                                                ql.price - COALESCE(ql.discount, 0),
                                                                                                'sarPrice',
                                                                                                (ql.price - COALESCE(ql.discount, 0)) * c.exchange_rate,
                                                                                                                                    'product',
                                                                                                JSON_BUILD_OBJECT(
                                                                                                        'id',
                                                                                                        p.id,
                                                                                                        'productNumber',
                                                                                                        p.product_number,
                                                                                                        'subBrandAr',
                                                                                                        sb.name_ar,
                                                                                                        'subBrandEn',
                                                                                                        sb.name_en
                                                                                                ),
                                                                                                'delivery',
                                                                                                q.receive_in
                                                                                        ))
                                                                        )
                                                                ), '[]') AS quotations
                                                FROM suppliers s
                                                         INNER join supplier_quotations q ON q.supplier_id = s.id
                                                         INNER JOIN supplier_quotation_lines ql
                                                                    ON ql.supplier_quotation_id = q.id
                                                         INNER JOIN products p ON ql.product_id = p.id
                                                         INNER JOIN currencies c ON q.currency_id = c.id
                                                         INNER JOIN brands sb ON p.sub_brand_id = sb.id
                                                WHERE (q.date >= :from_date AND q.date <= :to_date)
                                                  AND s.id in (SELECT supplier_id
                                                               FROM supplier_quotations q
                                                                        JOIN rfpq_lines rl
                                                                             ON rl.rfpq_id = q.rfpq_id
                                                                        JOIN products p ON rl.product_id = p.id)
                                                  And p.product_number in (SELECT product_number
                                                                           FROM products p
                                                                                    join rfpq_lines rl ON rl.product_id = p.id
                                                                           WHERE rl.rfpq_id = :specific_rfpq_id))
                                       ) AS result;
            """, nativeQuery = true)
    Object[] getSupplierQuotationsGrouped(@Param("specific_rfpq_id") Long rfpq, @Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate);
}
