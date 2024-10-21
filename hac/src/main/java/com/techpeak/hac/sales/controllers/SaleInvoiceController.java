package com.techpeak.hac.sales.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.sales.dtos.CreateSaleInvoiceRequest;
import com.techpeak.hac.sales.dtos.SaleInvoiceResponse;
import com.techpeak.hac.sales.dtos.SaleInvoiceResponseShort;
import com.techpeak.hac.sales.services.SaleInvoiceService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sale-invoices")
@RequiredArgsConstructor
@Tag(name = "Sale Invoices", description = "Sale Invoices API")
@Validated
public class SaleInvoiceController {
    private final SaleInvoiceService saleInvoiceService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateSaleInvoiceRequest request) {
        User user = AuthUtils.getCurrentUser();
        Long saleInvoiceId = saleInvoiceService.createSaleInvoice(request, user);
        return ResponseEntity.ok(saleInvoiceId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleInvoiceResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(saleInvoiceService.getOne(id));
    }

    @GetMapping
    public ResponseEntity<Page<SaleInvoiceResponseShort>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "ref", required = false) Long ref,
            @RequestParam(value = "customer", required = false) Long customer,
            @RequestParam(value = "user", required = false) Long user,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "invoice", required = false) String invoice) {
        return ResponseEntity.ok(saleInvoiceService.search(page, size, sort, ref, customer, user, date, invoice));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<SaleInvoiceResponseShort>> getAllActiveSaleInvoices() {
        return ResponseEntity.ok(saleInvoiceService.getAllActiveSaleInvoices());
    }
}