package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.ProductRequest;
import com.techpeak.hac.inventory.dtos.ProductResponse;
import com.techpeak.hac.inventory.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Long create(ProductRequest productRequest);

    Page<ProductResponse> listWithPages(Pageable pageRequest, Boolean isActive, String number);
    List<ProductResponse> list( Boolean isActive, String number, Boolean exactMatch);

    Product getProductOrThrow(Long id);

    ProductResponse get(Long id);
}
