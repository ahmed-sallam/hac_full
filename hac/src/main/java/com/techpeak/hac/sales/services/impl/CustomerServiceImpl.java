package com.techpeak.hac.sales.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.sales.dtos.CreateCustomer;
import com.techpeak.hac.sales.dtos.CustomerResponse;
import com.techpeak.hac.sales.dtos.mappers.CustomerMapper;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.repositories.CustomerRepository;
import com.techpeak.hac.sales.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(CreateCustomer createCustomer) {
        Customer customer = CustomerMapper.dtoToCustomer(createCustomer);
        return customerRepository.save(customer);
    }

    @Override
    public Page<CustomerResponse> search(Pageable pageRequest, String search, Boolean isActive) {
        Page<Customer> all = customerRepository.findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(
                isActive, search, search, pageRequest);
        return all.map(CustomerMapper::entityToCustomerResponse);
    }

    @Override
    public Customer get(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

    }

    @Override
    public CustomerResponse getCustomerResponse(Long id) {
        Customer custoemr = get(id);
        return CustomerMapper.entityToCustomerResponse(custoemr);
    }

    @Override
    public void update(Long id, CreateCustomer createCustomer) {
        Customer customer = get(id);
        customer.setNameAr(createCustomer.getNameAr());
        customer.setNameEn(createCustomer.getNameEn());
        customer.setEmail(createCustomer.getEmail());
        customer.setPhone(createCustomer.getPhone());
        customer.setAddress(createCustomer.getAddress());
        customer.setIsActive(createCustomer.getIsActive());
        customerRepository.save(customer);
    }
}
