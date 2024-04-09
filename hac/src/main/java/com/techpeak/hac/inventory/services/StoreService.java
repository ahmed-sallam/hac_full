package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateStore;
import com.techpeak.hac.inventory.dtos.StoreResponse;
import com.techpeak.hac.inventory.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {
void create(CreateStore createStore);
void update(Long id, CreateStore createStore);
Page<StoreResponse> list(Boolean isActive, String name, Pageable pageable);
Store getOrElseThrow(Long id);
StoreResponse getStore(Long id);
}
