package com.techpeak.hac.sales.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.InternalRefService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.dtos.InventoryTransactionEditRequest;
import com.techpeak.hac.inventory.dtos.InventoryTransactionRequest;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponse;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponseShort;
import com.techpeak.hac.inventory.enums.TransactionType;
import com.techpeak.hac.inventory.mappers.InventoryTransactionMapper;
import com.techpeak.hac.inventory.models.InventoryTransaction;
import com.techpeak.hac.inventory.models.InventoryTransactionLine;
import com.techpeak.hac.inventory.repositories.InventoryTransactionRepository;
import com.techpeak.hac.inventory.services.InventoryService;
import com.techpeak.hac.inventory.services.InventoryTransactionService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.StoreService;
import com.techpeak.hac.inventory.specifications.InventoryTransactionSpecification;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public Page<InventoryTransactionResponseShort> search(int page, int size, String sort,
                                                          String search, Long ref, Long store, Long user, String status,
                                                          String transactionType, String transactionDate, Long desStore) {

        Specification<InventoryTransaction> specification = InventoryTransactionSpecification
                .search(search, ref, store, user, status, transactionType, transactionDate, desStore);

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(sort.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sort.replace("-", "")));

        return repository.findAll(specification, pageRequest)
                .map(InventoryTransactionMapper::toResponse);
    }

    @Override
    public InventoryTransactionResponse findById(Long id) {
        List<Object[]> result = repository.findByIdWithLines(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Inventory Transaction not found with id: " + id);
        }

        InventoryTransaction transaction = (InventoryTransaction) result.get(0)[0];
        List<Object> userHistories = result.stream()
                .filter(row -> row[1] != null)
                .map(row -> row[1])
                .collect(Collectors.toList());

        return InventoryTransactionMapper.toDetailedResponse(transaction, userHistories);
    }


    @Override
    @Transactional
    public void changeStatus(Long id, RequestStatus status, User user) {
        InventoryTransaction transaction = getOrElseThrow(id);


        RequestStatus oldStatus = transaction.getStatus();

        // Prevent changing status if already in target status
        if (oldStatus == status) {
            throw new IllegalStateException("Transaction is already in " + status + " status");
        }

        // Update transaction status
        transaction.setStatus(status);

        // Handle inventory updates based on status change
        switch (status) {
            case CANCELED -> handleCancelStatus(transaction);
            case COMPLETED -> handleCompleteStatus(transaction);
            default ->
                    throw new IllegalArgumentException("Status change to " + status + " is not supported");
        }

        // Save the updated transaction
        repository.save(transaction);

        // Create user history entry
        String actionDetails =
                "Changed status from " + oldStatus + " to " + status;
        userHistoryService.createUserHistory(user, id, actionDetails, "inventory_transactions");
    }

    private InventoryTransaction getOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory Transaction not found with id: " + id));
    }

    private void handleCancelStatus(InventoryTransaction transaction) {
        // Reverse any inventory changes
        transaction.getLines().forEach(line -> {
            inventoryService.updateReservedQuantity(
                    line.getProduct(),
                    transaction.getStore(),
                    line.getQuantity() * -1
            );
            if (transaction.getDesiStore() != null) {
                inventoryService.updateReservedQuantity(
                        line.getProduct(),
                        transaction.getDesiStore(),
                        line.getQuantity()
                );
            }
        });
    }


    @Override
    @Transactional
    public void editTransaction(Long id, InventoryTransactionEditRequest request, User user) {
        InventoryTransaction transaction = getOrElseThrow(id);

        // Can only edit transactions that are not COMPLETED or CANCELED
        if (transaction.getStatus() == RequestStatus.COMPLETED ||
                transaction.getStatus() == RequestStatus.CANCELED) {
            throw new IllegalStateException("Cannot edit a " + transaction.getStatus() + " transaction");
        }

        // Handle existing line edits
        if (request.lines() != null) {
            for (var lineEdit : request.lines()) {
                var line = transaction.getLines().stream()
                        .filter(l -> l.getId().equals(lineEdit.id()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("Line not found: " + lineEdit.id()));

                if (Boolean.TRUE.equals(lineEdit.remove())) {
                    // Remove line and update inventory
                    transaction.getLines().remove(line);
                    inventoryService.updateReservedQuantity(
                            line.getProduct(),
                            transaction.getStore(),
                            line.getQuantity() * -1
                    );
                    if (transaction.getDesiStore() != null) {
                        inventoryService.updateReservedQuantity(
                                line.getProduct(),
                                transaction.getDesiStore(),
                                line.getQuantity() * -1
                        );
                    }
                } else if (lineEdit.doneQuantity() != null) {
                    // Update done quantity
                    if (lineEdit.doneQuantity() > line.getQuantity()) {
                        throw new IllegalArgumentException(
                                "Done quantity cannot exceed original quantity"
                        );
                    }
                    line.setDoneQuantity(lineEdit.doneQuantity());
                }
            }
        }
        // Handle new lines
        if (request.newLines() != null) {
            System.out.println("New lines: " + request.newLines());
            for (var newLine : request.newLines()) {
                var product = productService.getProductOrThrow(newLine.productId());
                var line = InventoryTransactionLine.builder()
                        .quantity(newLine.quantity())
                        .product(product)
                        .inventoryTransaction(transaction)  // Set the parent reference
                        .build();

                transaction.getLines().add(line);

                // Update reserved quantities for new lines
                inventoryService.updateReservedQuantity(
                        product,
                        transaction.getStore(),
                        newLine.quantity()
                );
                if (transaction.getDesiStore() != null) {
                    inventoryService.updateReservedQuantity(
                            product,
                            transaction.getDesiStore(),
                            newLine.quantity()
                    );
                }
            }
        }

        repository.save(transaction);

        String actionDetails = "Edited transaction lines and added new products";
        userHistoryService.createUserHistory(user, id, actionDetails, "inventory_transactions");
    }

    private void handleCompleteStatus(InventoryTransaction transaction) {
        transaction.getLines().forEach(line -> {
            // Update done quantity
            line.setDoneQuantity(line.getQuantity());

            // Update actual inventory quantities
            inventoryService.create(
                    line.getProduct(),
                    transaction.getStore(),
                    line.getQuantity() * -1
            );

            if (transaction.getDesiStore() != null) {
                inventoryService.create(
                        line.getProduct(),
                        transaction.getDesiStore(),
                        line.getQuantity()
                );
            }
        });
    }
}
