package com.techpeak.hac.sales.dtos.mappers;

import com.techpeak.hac.sales.dtos.CreateCustomer;
import com.techpeak.hac.sales.dtos.CustomerResponse;
import com.techpeak.hac.sales.models.Customer;

public class CustomerMapper {
    public static Customer dtoToCustomer(CreateCustomer createCustomer) {
        Customer customer = Customer.builder()
                .nameAr(createCustomer.getNameAr())
                .nameEn(createCustomer.getNameEn())
                .email(createCustomer.getEmail())
                .phone(createCustomer.getPhone())
                .address(createCustomer.getAddress())
                .build();
        customer.setIsActive(createCustomer.getIsActive());
        return customer;
    }

    public static CustomerResponse entityToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .nameAr(customer.getNameAr())
                .nameEn(customer.getNameEn())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .isActive(customer.getIsActive())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
