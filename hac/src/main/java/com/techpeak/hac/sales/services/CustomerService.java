package com.techpeak.hac.sales.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techpeak.hac.sales.dtos.CreateCustomer;
import com.techpeak.hac.sales.dtos.CustomerResponse;
import com.techpeak.hac.sales.models.Customer;

public interface CustomerService {
    Customer create(CreateCustomer createCustomer);

    Page<CustomerResponse> search(Pageable pageRequest, String search, Boolean isActive);

    Customer get(Long id);

    CustomerResponse getCustomerResponse(Long id);
}
