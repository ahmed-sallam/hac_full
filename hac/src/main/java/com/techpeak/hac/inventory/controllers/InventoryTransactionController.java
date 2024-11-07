package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.InventoryTransactionResponse;
import com.techpeak.hac.inventory.dtos.InventoryTransactionResponseShort;
import com.techpeak.hac.inventory.services.InventoryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory-transactions")
@RequiredArgsConstructor
@Tag(name = "inventory-transactions")
public class InventoryTransactionController {

    private final InventoryTransactionService service;

    @GetMapping("/search")
    @Operation(summary = "Search inventory transactions with filters")
    public ResponseEntity<Page<InventoryTransactionResponseShort>> search(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (prefix with - for DESC)") @RequestParam(defaultValue = "-id") String sort,
            @Parameter(description = "Search text") @RequestParam(required = false) String search,
            @Parameter(description = "Reference ID") @RequestParam(required = false) Long ref,
            @Parameter(description = "Store ID") @RequestParam(required = false) Long store,
            @Parameter(description = "User ID") @RequestParam(required = false) Long user,
            @Parameter(description = "Status") @RequestParam(required = false) String status,
            @Parameter(description = "Transaction type") @RequestParam(required = false) String transactionType,
            @Parameter(description = "Transaction date") @RequestParam(required = false) String transactionDate,
            @Parameter(description = "Destination store ID") @RequestParam(required = false) Long desStore) {

        return ResponseEntity.ok(service.search(
                page, size, sort, search, ref, store, user,
                status, transactionType, transactionDate, desStore));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory transaction by ID with lines and history")
    public ResponseEntity<InventoryTransactionResponse> findById(
            @Parameter(description = "Inventory Transaction ID") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
