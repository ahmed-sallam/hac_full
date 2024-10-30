package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "InventoryTransaction")
@Table(name = "inventory_transactions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class InventoryTransaction extends BaseEntity {
    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType = TransactionType.ADJUSTMENT;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @ToString.Exclude
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    private StoreLocation location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "des_store_id", nullable = false)
    @ToString.Exclude
    private Store desiStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "des_location_id")
    @ToString.Exclude
    private StoreLocation desLocation;
    // todo status
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate; // Better to use UTC
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;
}
