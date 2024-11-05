package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.enums.TransactionType;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @ToString.Exclude
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "des_store_id", nullable = false)
    @ToString.Exclude
    private Store desiStore;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate; // Better to use UTC

    @Column(name = "number", nullable = false, unique = true)
    private String number;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "inventoryTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<InventoryTransactionLine> lines =
            new HashSet<>();

    public void setLines(Set<InventoryTransactionLine> inventoryTransactionLines) {
        this.lines = inventoryTransactionLines;
        inventoryTransactionLines.forEach(inventoryTransactionLine -> inventoryTransactionLine.setInventoryTransaction(this));
        this.lines.addAll(inventoryTransactionLines);
    }
}
