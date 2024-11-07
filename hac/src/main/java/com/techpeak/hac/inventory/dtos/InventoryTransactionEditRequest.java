package com.techpeak.hac.inventory.dtos;

import java.util.List;

public record InventoryTransactionEditRequest(
    List<InventoryTransactionLineEdit> lines
) {
    public record InventoryTransactionLineEdit(
        Long id,
        Integer doneQuantity,
        Boolean remove
    ) {}
}
