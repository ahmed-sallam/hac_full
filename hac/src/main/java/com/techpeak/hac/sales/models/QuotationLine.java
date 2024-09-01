package com.techpeak.hac.sales.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "QuotationLine")
@Table(name = "quotation_lines", indexes = {
        @Index(name = "idx_sales_product_id", columnList = "product_id"),
        @Index(name = "idx_sales_quotation_id", columnList = "quotation_id")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QuotationLine extends BaseEntity {
    @Column(name = "quantity")
    private Integer quantity = 1;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "discount")
    private Double discount = 0d;
    @Column(name = "total")
    private Double total = 0d;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    @ToString.Exclude
    private Quotation quotation;
}
