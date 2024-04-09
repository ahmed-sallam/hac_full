package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.inventory.dtos.CreateStore;
import com.techpeak.hac.inventory.dtos.StoreResponse;
import com.techpeak.hac.inventory.mappers.StoreMapper;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.inventory.repositories.StoreRepository;
import com.techpeak.hac.inventory.services.StoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRespository;
    @Override
    public void create(CreateStore createStore) {
        Store store = StoreMapper.toEntity(createStore);
        storeRespository.save(store);
    }

    @Override
    public void update(Long id, CreateStore createStore) {
        Store store = getOrElseThrow(id);
        store.setNameAr(createStore.getNameAr());
        store.setNameEn(createStore.getNameEn());
        store.setCityAr(createStore.getCityAr());
        store.setCityEn(createStore.getCityEn());
        store.setIsActive(createStore.getIsActive());
        storeRespository.save(store);
    }

    @Override
    public Page<StoreResponse> list(Boolean isActive, String name, Pageable pageable) {
        Page<Store> all = storeRespository.findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(isActive, name, name, pageable);
        return all.map(StoreMapper::toDto);
    }

    @Override
    public Store getOrElseThrow(Long id) {
        return storeRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store with id " + id + " does not exist"));
    }

    @Override
    public StoreResponse getStore(Long id) {
        Store store = getOrElseThrow(id);
        StoreResponse dto = StoreMapper.toDto(store);
//        dto.setLocations(store.getLocations().stream()
//                .map(StoreLocationMapper::toStoreLocationResponse)
//                .collect(java.util.stream.Collectors.toList()));
        return dto;
    }
}
