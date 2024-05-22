package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "PurchaseDifference")
@Table(name = "purchase_differences")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseDifference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name = "demand", nullable = false)
    private Integer demand;
    @Column(name = "received", nullable = false)
    private Integer received=0;
    @Column(name = "invoiced", nullable = false)
    private Integer invoiced=0;
    @Column(name = "difference", nullable = false)
    private Integer difference=0;
    @Column(name = "invoice_price", nullable = false)
    private Double invoicePrice=0d;
    @Column(name = "cost", nullable = false)
    private Double cost=0d;
}
