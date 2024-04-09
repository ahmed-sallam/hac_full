package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.inventory.models.Product;

import java.time.LocalDateTime;

public record ProductsSetResponse(
        Long id, LocalDateTime createdAt,
        LocalDateTime updatedAt, Boolean isActive,
        Product product, Product productSet,
        Integer quantity) {
}