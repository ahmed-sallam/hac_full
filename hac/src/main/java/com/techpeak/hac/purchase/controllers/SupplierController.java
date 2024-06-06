package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.purchase.dtos.CreateSupplier;
import com.techpeak.hac.purchase.dtos.SupplierResponse;
import com.techpeak.hac.purchase.models.Supplier;
import com.techpeak.hac.purchase.services.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/suppliers")
@RequiredArgsConstructor
@Validated
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<Long> create(
            @Valid @RequestBody CreateSupplier createSupplier
    ) {
        Supplier supplier = supplierService.create(createSupplier);
        return new ResponseEntity<>(supplier.getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<SupplierResponse>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "name", defaultValue = "", required = false) String search,
            @RequestParam(value = "isActive", required = false, defaultValue = "true") Boolean isActive
    ) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<SupplierResponse> response = supplierService.search(pageRequest, search, isActive);
        return ResponseEntity.ok(response);
    }

}
