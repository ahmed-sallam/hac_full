package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.inventory.dtos.CreateProductSet;
import com.techpeak.hac.inventory.dtos.ProductsSetResponse;
import com.techpeak.hac.inventory.mappers.ProductSetMapper;
import com.techpeak.hac.inventory.models.ProductSet;
import com.techpeak.hac.inventory.repositories.ProductSetRepository;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.ProductSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSetServiceImpl implements ProductSetService {
    private final ProductSetRepository productSetRepository;
    private ProductService productService;
//    private final ProductService productService;

    @Autowired
    public void setProductService(@Lazy ProductService productService) {
        this.productService = productService;
    }
    @Override
    public void craete(CreateProductSet createProductSet) {
        ProductSet entity = ProductSetMapper.toEntity(createProductSet);
        entity.setProduct(productService.getProductOrThrow(createProductSet.getProductId()));
        entity.setProductSet(productService.getProductOrThrow(createProductSet.getProductSetId()));
        productSetRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        ProductSet productSet = getProductSetOrThrow(id);
        productSetRepository.delete(productSet);
    }

    @Override
    public void update(Long id, Integer quantity) {
        ProductSet productSet = getProductSetOrThrow(id);
        if(quantity < 1){
            throw new RuntimeException("Quantity must be greater than 1");
        }
        productSet.setQuantity(quantity);
        productSetRepository.save(productSet);
    }

    @Override
    public ProductsSetResponse get(Long id) {
        ProductSet productSet = getProductSetOrThrow(id);
        return ProductSetMapper.toDto(productSet);
    }

    @Override
    public List<ProductsSetResponse> getAll(boolean active) {
        List<ProductSet> allByIsActive = productSetRepository.findAllByIsActive(active);
        return allByIsActive.stream()
                .map(ProductSetMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductSet getProductSetOrThrow(Long id) {
        return productSetRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product set with id " + id + "not found"));
    }
}
