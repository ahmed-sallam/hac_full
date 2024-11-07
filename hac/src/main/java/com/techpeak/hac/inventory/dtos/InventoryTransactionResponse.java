package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.inventory.enums.TransactionType;
import com.techpeak.hac.purchase.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

public record InventoryTransactionResponse(
        String number,
        TransactionType transactionType,
        RequestStatus status,
        StoreResponseShort store,
        StoreResponseShort desiStore,
        LocalDateTime transactionDate,
        Long internalRefId,
        String currentPhase,
        List<InventoryTransactionLineResponse> lines,
        List<UserHistoryResponse> userHistories
) {
}
