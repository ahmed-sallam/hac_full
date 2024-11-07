package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.inventory.dtos.InventoryTransactionLineResponse;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponse;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponseShort;
import com.techpeak.hac.inventory.models.InventoryTransaction;
import com.techpeak.hac.inventory.models.InventoryTransactionLine;
import com.techpeak.hac.purchase.mappers.UserHistoryMapper;

import java.util.List;
import java.util.stream.Collectors;

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


    public static InventoryTransactionResponse toDetailedResponse(
            InventoryTransaction transaction,
            List<Object> userHistories) {
        return new InventoryTransactionResponse(
                transaction.getNumber(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                StoreMapper.toShort(transaction.getStore()),
                StoreMapper.toShort(transaction.getDesiStore()),
                transaction.getTransactionDate(),
                transaction.getInternalRef().getId(),
                transaction.getInternalRef().getCurrentPhase().name(),
                transaction.getLines().stream()
                        .map(InventoryTransactionMapper::toLineResponse)
                        .collect(Collectors.toList()),
                userHistories.stream()
                        .map(history -> UserHistoryMapper.mapToDto((UserHistory) history))
                        .collect(Collectors.toList())
        );
    }

    private static InventoryTransactionLineResponse toLineResponse(InventoryTransactionLine line) {
        return new InventoryTransactionLineResponse(
                line.getId(),
                ProductMapper.mapToProductResponseShort(line.getProduct()),
                line.getQuantity(),
                line.getDoneQuantity()
        );
    }

}
