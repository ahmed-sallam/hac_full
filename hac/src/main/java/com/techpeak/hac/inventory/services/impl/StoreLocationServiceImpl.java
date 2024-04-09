package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.inventory.dtos.CreateStoreLocation;
import com.techpeak.hac.inventory.dtos.StoreLocationResponse;
import com.techpeak.hac.inventory.mappers.StoreLocationMapper;
import com.techpeak.hac.inventory.models.StoreLocation;
import com.techpeak.hac.inventory.repositories.StoreLocationRepository;
import com.techpeak.hac.inventory.services.StoreLocationService;
import com.techpeak.hac.inventory.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreLocationServiceImpl implements StoreLocationService {
    private final StoreLocationRepository storeLocationRepository;
    private final StoreService storeService;
    @Override
    public void create(Long storeId, CreateStoreLocation createStoreLocation) {
        StoreLocation storeLocation = StoreLocationMapper.toStoreLocation(createStoreLocation);
        storeLocation.setStore(storeService.getOrElseThrow(storeId));
        storeLocationRepository.save(storeLocation);
    }

    @Override
    public void update(Long id, CreateStoreLocation createStoreLocation) {
        StoreLocation storeLocation = getOrElseThrow(id);
        storeLocation.setNameAr(createStoreLocation.getNameAr());
        storeLocation.setNameEn(createStoreLocation.getNameEn());
        storeLocation.setIsActive(createStoreLocation.getIsActive());
        storeLocation.setStore(storeService.getOrElseThrow(createStoreLocation.getStoreId()));
        storeLocationRepository.save(storeLocation);
    }

    @Override
    public StoreLocation getOrElseThrow(Long id) {
        return storeLocationRepository.findById(id).orElseThrow(() -> new RuntimeException("Store location with id " + id + " does not exist"));
    }

    @Override
    public List<StoreLocationResponse> list() {
        return storeLocationRepository.findAll().stream()
                .map(StoreLocationMapper::toStoreLocationResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
