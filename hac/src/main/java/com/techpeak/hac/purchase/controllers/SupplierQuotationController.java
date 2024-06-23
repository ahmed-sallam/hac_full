package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.purchase.dtos.SupplierQuotationGroubBySupplier;
import com.techpeak.hac.purchase.dtos.SupplierQuotationRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponse;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponseShort;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import com.techpeak.hac.purchase.services.SupplierQuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier-quotations")
@RequiredArgsConstructor
public class SupplierQuotationController {
    private final SupplierQuotationService supplierQuotationService;

    @GetMapping
    public ResponseEntity<Page<SupplierQuotationResponseShort>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "ref", required = false) Long ref,
            @RequestParam(value = "supplier", required = false) Long supplier,
            @RequestParam(value = "user", required = false) Long user,
            @RequestParam(value = "supplierRef", required = false) String supplierRef,
            @RequestParam(value = "isLocal", required = false) Boolean isLocal,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "rfpq", required = false) String rfpq

    ) {
        return ResponseEntity.ok(supplierQuotationService.search(page, size, sort, ref, supplier, user, supplierRef, isLocal, date, rfpq));
    }
    // I want to get all supplier quotations (paginated) by is active or without it ,  with the following fields:
    // id,  date, supplier name , currency,  total, isLocal, paymentTerms, supplierRef, internalRef, user, rfpq

    @GetMapping("/{id}")
    public ResponseEntity<SupplierQuotationResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(supplierQuotationService.getOne(id));
    }
    @GetMapping("/active")
    public ResponseEntity<Page<SupplierQuotationResponseShort>> getAllActiveSupplierQuotations() {
        return ResponseEntity.ok(supplierQuotationService.getAllActiveSupplierQuotations());
    }
    // create new supplier quotation
    @PostMapping
    public ResponseEntity<Long> createSupplierQuotation(@RequestBody SupplierQuotationRequest request) {
        User user = AuthUtils.getCurrentUser();
        SupplierQuotation supplierQuotation = supplierQuotationService.createSupplierQuotation(request, user);
        return ResponseEntity.ok(supplierQuotation.getId());
    }

    @GetMapping("/group-by-supplier")
    public ResponseEntity<List<SupplierQuotationGroubBySupplier>> getSupplierQuotationsGroupBySupplier(@RequestParam("productNumber") String productNumber, @RequestParam("fromDate") LocalDate fromDate) {
        return ResponseEntity.ok(supplierQuotationService.getSupplierQuotationsGroupBySupplier(productNumber, fromDate));
    }
}
