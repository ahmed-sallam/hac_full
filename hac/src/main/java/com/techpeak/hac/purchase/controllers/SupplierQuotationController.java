package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.purchase.dtos.SupplierQuotationRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponseShort;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import com.techpeak.hac.purchase.services.SupplierQuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/supplier-quotations")
@RequiredArgsConstructor
public class SupplierQuotationController {
    private final SupplierQuotationService supplierQuotationService;

    @GetMapping
    public ResponseEntity<Page<SupplierQuotationResponseShort>> getAllSupplierQuotations() {
        return ResponseEntity.ok(supplierQuotationService.getAllSupplierQuotations());
    }
    // I want to get all supplier quotations (paginated) by is active or without it ,  with the following fields:
    // id,  date, supplier name , currency,  total, isLocal, paymentTerms, supplierRef, internalRef, user, rfpq

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
}
