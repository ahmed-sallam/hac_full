package com.techpeak.hac.inventory.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.dtos.InventoryTransactionRequest;

public interface InventoryTransactionService {
    void createInventoryTransaction(InventoryTransactionRequest request,
                                    User user);

    void cancelInventoryTransaction(Long id, User user);
}
