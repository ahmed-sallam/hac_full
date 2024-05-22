package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "PurchaseOrder")
@Table(name = "purchase_orders", indexes = {@Index(name = "idx_purchase_orders_number", columnList = "number")})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseOrder extends BaseEntity {
    private String number;
    private String notes;
    private LocalDate date;
    @Column(name="status",nullable = false )
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    @ManyToOne
    @JoinColumn(name="store_id" , nullable = false)
    private Store store;
    @OneToOne
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @Column(name = "supplier_ref")
    private String supplierRef;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @OneToMany(mappedBy = "purchaseOrder")
    private Set<PurchaseOrderLine> lines;

    @Column(name = "sub_total")
    private Double subTotal;
    private Double vat;
    private Double total;
}
