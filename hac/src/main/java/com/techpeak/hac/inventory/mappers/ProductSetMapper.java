package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.CreateProductSet;
import com.techpeak.hac.inventory.dtos.ProductsSetResponse;
import com.techpeak.hac.inventory.dtos.ProductsSetResponseSetOnly;
import com.techpeak.hac.inventory.models.ProductSet;

public class ProductSetMapper {
    public static ProductSet toEntity(CreateProductSet createProductSet) {
        ProductSet productSet = ProductSet
                .builder()
                .quantity(createProductSet.getQuantity())
                .isRestricted(createProductSet.getIsRestricted())
                .build();
        return productSet;
    }

    public static ProductsSetResponse toDto(ProductSet productSet) {
        return new ProductsSetResponse(
                productSet.getId(),
                productSet.getCreatedAt(),
                productSet.getUpdatedAt(),
                productSet.getIsActive(),
                productSet.getProduct(),
                productSet.getProductSet(),
                productSet.getQuantity(),
                productSet.getIsRestricted()
        );
    }
    private static ProductsSetResponseSetOnly toDtoSetOnly(ProductSet productSet) {
        return new ProductsSetResponseSetOnly(
                productSet.getId(),
                productSet.getCreatedAt(),
                productSet.getUpdatedAt(),
                productSet.getIsActive(),
                productSet.getProduct().getId(),
                productSet.getProductSet(),
                productSet.getQuantity(),
                productSet.getIsRestricted()
        );
    }
}
