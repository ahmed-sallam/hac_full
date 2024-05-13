package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private StoreLocation location;

    @Column( name = "transaction_date")
    private LocalDateTime transactionDate; // Better to use UTC
}
