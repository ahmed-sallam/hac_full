package com.techpeak.hac.sales.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.InternalRefService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.dtos.InventoryTransactionRequest;
import com.techpeak.hac.inventory.enums.TransactionType;
import com.techpeak.hac.inventory.models.InventoryTransaction;
import com.techpeak.hac.inventory.models.InventoryTransactionLine;
import com.techpeak.hac.inventory.repositories.InventoryTransactionRepository;
import com.techpeak.hac.inventory.services.InventoryService;
import com.techpeak.hac.inventory.services.InventoryTransactionService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.StoreService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryTransactionServiceImpl implements InventoryTransactionService {
    private final InventoryTransactionRepository repository;
    private final StoreService storeService;
    private final InternalRefService internalRefService;
    private final ProductService productService;
    private final UserHistoryService userHistoryService;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public void createInventoryTransaction(InventoryTransactionRequest request, User user) {
        Optional<InventoryTransaction> lastOne = repository.findTopByOrderByNumberDesc();
        String number = GenerateRequestNumber.generateRequestNumber("INV",
                lastOne.map(InventoryTransaction::getNumber).orElse(null));
        InternalRef internalRef = request.internalRefId() != null ?
                internalRefService.getInternalRefById(request.internalRefId()) :
                new InternalRef();
        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .number(number)
                .transactionType(TransactionType.valueOf(request.transactionType()))
                .status(RequestStatus.valueOf(request.status()))
                .store(storeService.getOrElseThrow(request.storeId()))
                .desiStore(storeService.getOrElseThrow(request.desiStoreId()))
                .transactionDate(request.transactionDate())
                .internalRef(internalRef)
                .user(user)
                .build();
        inventoryTransaction.setLines(request.lines().stream()
                .map(line -> InventoryTransactionLine.builder()
                        .quantity(line.quantity())
                        .product(productService.getProductOrThrow(line.productId()))
                        .cost(line.cost())
                        .build())
                .collect(Collectors.toSet()));
        InventoryTransaction saved = repository.save(inventoryTransaction);
        String actionDetails = "Created a new Inventory Transaction  related " +
                "to internal id: " + saved.getInternalRef().getId() + " with " +
                "number " + saved.getNumber();
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "inventory_transactions");
        // update or create inventory.
        // todo make it better
        request.lines()
                .forEach(line -> {
                    inventoryService.create(productService.getProductOrThrow(line.productId()), inventoryTransaction.getStore(), line.quantity() * -1);
                    inventoryService.create(productService.getProductOrThrow(line.productId()), inventoryTransaction.getDesiStore(), line.quantity());
                });
    }

    @Override
    @Transactional
    public void cancelInventoryTransaction(Long refId, User user) {
        // get the inventory transaction by internal ref id
        Optional<InventoryTransaction> inventoryTransaction = repository.findByInternalRefId(refId);
        if (inventoryTransaction.isEmpty()) {
            throw new NotFoundException("Inventory Transaction not found with id " + refId);
        }
        inventoryTransaction.get().setStatus(RequestStatus.CANCELED);
        repository.save(inventoryTransaction.get());
        String actionDetails = "Canceled Inventory Transaction with number: " + inventoryTransaction.get().getNumber();
        userHistoryService.createUserHistory(user, inventoryTransaction.get().getId(), actionDetails, "inventory_transactions");
        // updated inventory
        inventoryTransaction.get().getLines().forEach(line -> {
            inventoryService.updateReservedQuantity(line.getProduct(),
                    inventoryTransaction.get().getStore(),
                    line.getQuantity() * -1);
            inventoryService.updateReservedQuantity(line.getProduct(), inventoryTransaction.get().getDesiStore(), line.getQuantity());
        });

    }

}
