package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateProductSet;
import com.techpeak.hac.inventory.dtos.ProductsSetResponse;
import com.techpeak.hac.inventory.models.ProductSet;

import java.util.List;

public interface ProductSetService {
    void craete(CreateProductSet createProductSet);

    void delete(Long id);

    void update(Long id, Integer quantity);
    ProductsSetResponse get(Long id);
    List<ProductsSetResponse> getAll(boolean active);
    ProductSet getProductSetOrThrow(Long id);
}
