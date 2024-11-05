package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "InventoryTransactionLine")
@Table(name = "inventory_transaction_lines", indexes = {
        @Index(name = "idx_inventory_transaction_line_product_id", columnList = "product_id"),
        @Index(name = "idx_inventory_transaction_id", columnList =
                "inventory_transaction_id")

})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransactionLine extends BaseEntity {

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @ToString.Exclude
    private StoreLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "des_location_id")
    @ToString.Exclude
    private StoreLocation desLocation;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_transaction_id")
    @ToString.Exclude
    private InventoryTransaction inventoryTransaction;
}
