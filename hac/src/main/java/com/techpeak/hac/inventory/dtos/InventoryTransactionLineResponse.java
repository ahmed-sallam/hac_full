package com.techpeak.hac.inventory.dtos;

public record InventoryTransactionLineResponse(
        Long id,
        ProductResponseShort product,
        Integer quantity,
        Integer doneQuantity
) {
}
