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
    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private SupplierQuotation quotation;
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
