package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "PurchaseOrderLine")
@Table(name = "purchase_order_lines")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseOrderLine extends BaseEntity {
    @Column(name = "quantity")
    private Integer quantity = 1;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "vat")
    private Double vat = 0d;
    @Column(name = "total")
    private Double total = 0d;
    @Column(name = "notes")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;
}
