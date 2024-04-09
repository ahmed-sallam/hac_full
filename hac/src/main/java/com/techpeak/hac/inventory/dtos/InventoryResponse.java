package com.techpeak.hac.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.Inventory}
 */

@Builder
public record InventoryResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isActive,
                                Integer qunatity, ProductResponse product, StoreResponse store,
                                StoreLocationResponse location) implements Serializable {
}