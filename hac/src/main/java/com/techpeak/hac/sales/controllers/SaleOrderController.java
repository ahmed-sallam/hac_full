package com.techpeak.hac.sales.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.sales.dtos.CreateSaleOrder;
import com.techpeak.hac.sales.dtos.SaleOrderResponse;
import com.techpeak.hac.sales.dtos.SaleOrderResponseShort;
import com.techpeak.hac.sales.services.SaleOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sale-orders")
@RequiredArgsConstructor
@Tag(name = "Sale Orders", description = "Sale Orders API")
@Validated
public class SaleOrderController {
    private final SaleOrderService saleOrderService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSaleOrder request) {
        User user = AuthUtils.getCurrentUser();
        Long saleOrderId = saleOrderService.createSaleOrder(request, user);
        return ResponseEntity.ok(saleOrderId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleOrderResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(saleOrderService.getOne(id));
    }

    @GetMapping
    public ResponseEntity<Page<SaleOrderResponseShort>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "ref", required = false) Long ref,
            @RequestParam(value = "customer", required = false) Long customer,
            @RequestParam(value = "user", required = false) Long user,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "order", required = false) String order) {
        return ResponseEntity.ok(saleOrderService.search(page, size, sort, ref, customer, user, date, order));
    }

//    @GetMapping("/active")
//    public ResponseEntity<Page<SaleOrderResponseShort>> getAllActiveSaleOrders() {
//        return ResponseEntity.ok(saleOrderService.getAllActiveSaleOrders());
//    }
    
}
