package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.CreateStore;
import com.techpeak.hac.inventory.dtos.CreateStoreLocation;
import com.techpeak.hac.inventory.dtos.StoreResponse;
import com.techpeak.hac.inventory.services.StoreLocationService;
import com.techpeak.hac.inventory.services.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "stores")
public class StoreController {
    private final StoreService storeService;
    private final StoreLocationService storeLocationService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateStore createStore) {
        storeService.create(createStore);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateStore createStore) {
        storeService.update(id, createStore);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(storeService.getStore(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<StoreResponse>> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "80") Integer size,
                                                    @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive,
                                                    @RequestParam(value = "name", defaultValue = "") String name
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(storeService.list(isActive, name, pageRequest), HttpStatus.OK);
    }

@PostMapping("/{storeId}/locations")
public ResponseEntity<?> createLocation(@PathVariable Long storeId, @RequestBody CreateStoreLocation createStoreLocation) {
    storeLocationService.create(storeId, createStoreLocation);
    return new ResponseEntity<>(HttpStatus.CREATED);
}

@PutMapping("/{storeId}/locations/{id}")
public ResponseEntity<?> updateLocation(@PathVariable Long storeId, @PathVariable Long id, @RequestBody CreateStoreLocation createStoreLocation) {
    storeLocationService.update(id, createStoreLocation);
    return new ResponseEntity<>(HttpStatus.OK);
}

}
