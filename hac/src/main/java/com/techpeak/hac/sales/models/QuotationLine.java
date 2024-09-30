package com.techpeak.hac.sales.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "QuotationLine")
@Table(name = "quotation_lines", indexes = {
        @Index(name = "idx_sales_product_id", columnList = "product_id"),
        @Index(name = "idx_sales_quotation_id", columnList = "quotation_id")
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationLine extends BaseEntity {

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount", nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total", nullable = false)
    private BigDecimal lineTotal = BigDecimal.ZERO;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    @ToString.Exclude
    private Quotation quotation;

   
}
