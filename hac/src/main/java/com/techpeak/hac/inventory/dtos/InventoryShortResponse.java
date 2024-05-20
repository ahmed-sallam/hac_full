package com.techpeak.hac.inventory.dtos;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.Inventory}
 */

public record InventoryShortResponse(Long id, Integer quantity, String storeNameAr, String storeNameEn,
                                     String locationNameAr, String locationNameEn, Long storeId,
                                     Long locationID) implements Serializable {
}