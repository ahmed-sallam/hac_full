package com.techpeak.hac.inventory.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.dtos.InventoryTransactionRequest;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponse;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponseShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import org.springframework.data.domain.Page;

public interface InventoryTransactionService {
    void createInventoryTransaction(InventoryTransactionRequest request,
                                    User user);

    void cancelInventoryTransaction(Long id, User user);

    Page<InventoryTransactionResponseShort> search(int page, int size,
                                                   String sort,
                                                   String search,
                                                   Long ref,
                                                   Long store,
                                                   Long user,
                                                   String status,
                                                   String transactionType,
                                                   String transactionDate,
                                                   Long desStore);

    InventoryTransactionResponse findById(Long id);

    void changeStatus(Long id, RequestStatus status, User user);
}
