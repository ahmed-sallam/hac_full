package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateStoreLocation;
import com.techpeak.hac.inventory.dtos.StoreLocationResponse;
import com.techpeak.hac.inventory.models.StoreLocation;

import java.util.List;

public interface StoreLocationService {
    void create(Long storeId, CreateStoreLocation createStoreLocation);
    void update(Long id, CreateStoreLocation createStoreLocation);
    List<StoreLocationResponse> list();
     StoreLocation getOrElseThrow(Long id);
}
