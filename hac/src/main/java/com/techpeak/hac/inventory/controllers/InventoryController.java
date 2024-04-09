package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.CreateInventory;
import com.techpeak.hac.inventory.dtos.InventoryResponse;
import com.techpeak.hac.inventory.dtos.InventoryShortResponse;
import com.techpeak.hac.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<Page<InventoryResponse>> listWithPages(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "productNumber", defaultValue = "") String productNumber) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<InventoryResponse> data = inventoryService.listWithPages(pageRequest, productNumber);
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateInventory createInventory) throws Exception {
        inventoryService.create(createInventory);
        return ResponseEntity.created(null).build();
    }

//    @GetMapping("/products/{id}")
//    public ResponseEntity<List<InventoryResponse>> getByProductId(@PathVariable("id") Long id)  {
//        List<InventoryResponse> inventory = inventoryService.getByProductId(id);
//        return ResponseEntity.ok(inventory);
//    }
        @GetMapping("/products/{id}")
    public ResponseEntity<List<InventoryShortResponse>> getByProductId(@PathVariable("id") Long id)  {
        List<InventoryShortResponse> inventory = inventoryService.getByProductId(id);
        return ResponseEntity.ok(inventory);
    }
}
