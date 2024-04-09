package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.InventoryResponse;
import com.techpeak.hac.inventory.models.Inventory;

public class InventoryMapper {

    public static InventoryResponse toResponse(Inventory inventory) {
        return InventoryResponse
                .builder()
                .id(inventory.getId())
                .store(StoreMapper.toDto(inventory.getStore()))
                .product(ProductMapper.toDto(inventory.getProduct()))
                .location(inventory.getLocation() != null ? StoreLocationMapper.toStoreLocationResponse(inventory.getLocation()) : null)
                .qunatity(inventory.getQuantity())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }


}
