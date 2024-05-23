package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "BidSummaryLine")
@Table(name = "bid_summary_lines")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BidSummaryLine extends BaseEntity {
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "discount")
    private Double discount = 0d;
    @Column(name = "vat")
    private Double vat = 0d;
    @Column(name = "sub_total")
    private Double subTotal = 0d;
    @Column(name = "expenses")
    private Double expenses;
    @Column(name = "total")
    private Double total = 0d;
    @Column(name = "is_local")
    private Boolean isLocal = true;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @ManyToOne
    @JoinColumn(name = "bid_summary_id")
    private BidSummary bidSummary;
}
