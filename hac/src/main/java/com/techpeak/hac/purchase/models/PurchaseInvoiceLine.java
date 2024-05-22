package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "PurchaseInvoiceLine")
@Table(name = "purchase_invoice_lines")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseInvoiceLine extends BaseEntity {
    @Column(name = "quantity")
    private Integer quantity = 1;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "discount")
    private Double discount = 0d;
    @Column(name = "vat")
    private Double vat = 0d;
    @Column(name = "total")
    private Double total = 0d;
    @Column(name = "notes")
    private Integer notes;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_invoice_id", nullable = false)
    private PurchaseInvoice purchaseInvoice;
}
