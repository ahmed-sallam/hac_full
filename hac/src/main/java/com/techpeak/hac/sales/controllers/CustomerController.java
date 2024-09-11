package com.techpeak.hac.sales.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpeak.hac.sales.dtos.CreateCustomer;
import com.techpeak.hac.sales.dtos.CustomerResponse;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.services.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/customers")
@Tag(name = "Customer", description = "Customer API")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Long> create(
            @Valid @RequestBody CreateCustomer createCustomer) {
        Customer customer = customerService.create(createCustomer);
        return new ResponseEntity<>(customer.getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "name", defaultValue = "", required = false) String search,
            @RequestParam(value = "isActive", required = false, defaultValue = "true") Boolean isActive) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<CustomerResponse> response = customerService.search(pageRequest, search, isActive);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable("id") Long id) {
        CustomerResponse response = customerService.getCustomerResponse(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable("id") Long id, @RequestBody CreateCustomer create) {
        customerService.update(id, create);
        return ResponseEntity.ok().build();
    }
}
