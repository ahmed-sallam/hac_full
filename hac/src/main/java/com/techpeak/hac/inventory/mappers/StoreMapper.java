package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.CreateStore;
import com.techpeak.hac.inventory.dtos.StoreResponse;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.inventory.models.StoreLocation;

import java.util.Set;

public class StoreMapper {
    public static Store toEntity(CreateStore createStore) {
        Store store =  Store.builder()
                .nameAr(createStore.getNameAr())
                .nameEn(createStore.getNameEn())
                .cityAr(createStore.getCityAr())
                .cityEn(createStore.getCityEn())
                        .build();
        store.setIsActive(createStore.getIsActive());
        return store;
    }

    public static StoreResponse toDto(Store store){
        StoreResponse storeResponse = new StoreResponse(
        );
        storeResponse.setId(store.getId());
        storeResponse.setNameAr(store.getNameAr());
        storeResponse.setNameEn(store.getNameEn());
        storeResponse.setCityAr(store.getCityAr());
        storeResponse.setCityEn(store.getCityEn());
        storeResponse.setIsActive(store.getIsActive());
        storeResponse.setCreatedAt(store.getCreatedAt());
        storeResponse.setUpdatedAt(store.getUpdatedAt());
        Set<StoreLocation> locations = store.getLocations();
        storeResponse.setLocations(locations.stream()
                .map(StoreLocationMapper::toStoreLocationResponse)
                .collect(java.util.stream.Collectors.toList()));
        return storeResponse;
    }
}
