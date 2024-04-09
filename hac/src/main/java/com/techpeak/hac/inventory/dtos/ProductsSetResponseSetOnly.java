package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.inventory.models.Product;

import java.time.LocalDateTime;

public record ProductsSetResponseSetOnly(
        Long id, LocalDateTime createdAt,
        LocalDateTime updatedAt, Boolean isActive,
        Long product, Product productSet,
        Integer quantity
) {
}