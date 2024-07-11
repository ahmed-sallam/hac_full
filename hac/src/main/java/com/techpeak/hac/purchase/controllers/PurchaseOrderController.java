package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.purchase.dtos.purchase_order.CreatePurchaseOrder;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderDto;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.PurchaseOrder;
import com.techpeak.hac.purchase.services.PurchaseOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@Tag(name = "Purchase Order", description = "Purchase Order API")
@RequiredArgsConstructor
@Validated
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<Long> createPurchaseOrder(@RequestBody @Valid CreatePurchaseOrder purchaseOrderDto) {
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrderDto);
        return ResponseEntity.ok(purchaseOrder.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDto> getPurchaseOrder(@PathVariable Long id) {
        PurchaseOrderDto purchaseOrder = purchaseOrderService.getPurchaseOrder(id);
        return ResponseEntity.ok(purchaseOrder);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePurchaseOrderStatus(@PathVariable Long id, @RequestBody RequestStatus status) {
        User user = AuthUtils.getCurrentUser();
        purchaseOrderService.updatePurchaseOrderStatus(id, status, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePurchaseOrder(@PathVariable Long id, @RequestBody @Valid CreatePurchaseOrder purchaseOrderDto) {
        purchaseOrderService.updatePurchaseOrder(id, purchaseOrderDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<PurchaseOrderShort>> getPurchaseOrders(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "ref", required = false) Long ref,
            @RequestParam(value = "store", required = false) Long store,
            @RequestParam(value = "user", required = false) Long user,
            @RequestParam(value = "phase", required = false) String phase,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "supplier", required = false) Long supplier

    ) {
        Page<PurchaseOrderShort> purchaseOrders = purchaseOrderService
                .getPurchaseOrders(page, size, sort, search, ref, store, user, phase, status, supplier);
        return ResponseEntity.ok(purchaseOrders);
    }

}
