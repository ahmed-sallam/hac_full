package com.techpeak.hac.inventory.dtos;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.Inventory}
 */
@Getter
@ToString
public class InventoryShortResponse implements Serializable {
    private final Long id;
    private final Integer quantity;
    private final String storeNameAr;
    private final String storeNameEn;
    private final String locationNameAr;
    private final String locationNameEn;

    public InventoryShortResponse(Long id, Integer quantity, String storeNameAr, String storeNameEn, String locationNameAr, String locationNameEn) {
        this.id = id;
        this.quantity = quantity;
        this.storeNameAr = storeNameAr;
        this.storeNameEn = storeNameEn;
        this.locationNameAr = locationNameAr;
        this.locationNameEn = locationNameEn;
    }
}