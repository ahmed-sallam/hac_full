package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.CreateStoreLocation;
import com.techpeak.hac.inventory.dtos.StoreLocationResponse;
import com.techpeak.hac.inventory.models.StoreLocation;

public class StoreLocationMapper {
    public static StoreLocationResponse toStoreLocationResponse(StoreLocation storeLocation) {
        StoreLocationResponse storeLocationResponse = new com.techpeak.hac.inventory.dtos.StoreLocationResponse();
        storeLocationResponse.setId(storeLocation.getId());
        storeLocationResponse.setNameAr(storeLocation.getNameAr());
        storeLocationResponse.setNameEn(storeLocation.getNameEn());
        storeLocationResponse.setStoreId(storeLocation.getStore().getId());
        storeLocationResponse.setIsActive(storeLocation.getIsActive());
        storeLocationResponse.setCreatedAt(storeLocation.getCreatedAt());
        storeLocationResponse.setUpdatedAt(storeLocation.getUpdatedAt());
        return storeLocationResponse;
    }

    public static StoreLocation toStoreLocation(CreateStoreLocation createStoreLocation) {
        StoreLocation storeLocation = new StoreLocation();
        storeLocation.setNameAr(createStoreLocation.getNameAr());
        storeLocation.setNameEn(createStoreLocation.getNameEn());
        storeLocation.setIsActive(createStoreLocation.getIsActive());
        return storeLocation;
    }
}
