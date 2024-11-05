package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.InventoryTransaction}
 */
public record InventoryTransactionRequest(
        @NotNull @PastOrPresent LocalDateTime transactionDate, Long storeId,
        Long desiStoreId,
        @NotNull
        @jakarta.validation.constraints.Pattern(regexp =
                "TRANSFER|SALE|PURCHASE|ADJUSTMENT|PURCHASE_RETURN" +
                        "|SALE_RETURN|OPENING_BALANCE|SCRAP") String transactionType,
        @NotNull @jakarta.validation.constraints.Pattern(regexp =
                "DRAFT|PENDING|PROCESSING|COMPLETED|CANCELED") String status,
        Long internalRefId,
        @NotNull @Size(min = 1) Set<InventoryTransactionLineRequest> lines) implements Serializable {
}
