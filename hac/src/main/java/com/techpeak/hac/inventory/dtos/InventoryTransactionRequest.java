package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.inventory.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.InventoryTransaction}
 */
public record InventoryTransactionRequest(
        @NotNull TransactionType transactionType,
        @NotNull @Positive Integer quantity, Long productId, Long storeId,
        Long locationId, Long desiStoreId, Long desLocationId,
        @NotNull @PastOrPresent LocalDateTime transactionDate) implements Serializable {
}
