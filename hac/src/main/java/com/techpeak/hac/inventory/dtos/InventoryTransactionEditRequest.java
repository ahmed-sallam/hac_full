package com.techpeak.hac.inventory.dtos;

import java.util.List;

public record InventoryTransactionEditRequest(
    List<InventoryTransactionLineEdit> lines,
    List<NewInventoryTransactionLine> newLines
) {
    public record InventoryTransactionLineEdit(
        Long id,
        Integer doneQuantity,
        Boolean remove
    ) {}

    public record NewInventoryTransactionLine(
        Long productId,
        Integer quantity
    ) {}
}
