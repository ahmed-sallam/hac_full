package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponseShort;
import com.techpeak.hac.inventory.models.InventoryTransaction;

public class InventoryTransactionMapper {
    public static InventoryTransactionResponseShort toResponse(InventoryTransaction inventoryTransaction) {
        return InventoryTransactionResponseShort
                .builder()
                .id(inventoryTransaction.getId())
                .number(inventoryTransaction.getNumber())
                .internalRef(inventoryTransaction.getInternalRef().getId())
                .user(UserMapper.toDtoShort(inventoryTransaction.getUser()))
                .currentPhase(inventoryTransaction.getInternalRef().getCurrentPhase().name())
                .transactionType(inventoryTransaction.getTransactionType().name())
                .transactionDate(inventoryTransaction.getTransactionDate().toString())
                .store(StoreMapper.toShort(inventoryTransaction.getStore()))
                .desStore(StoreMapper.toShort(inventoryTransaction.getDesiStore()))
                .status(inventoryTransaction.getStatus().name())
                .build();
    }
}
