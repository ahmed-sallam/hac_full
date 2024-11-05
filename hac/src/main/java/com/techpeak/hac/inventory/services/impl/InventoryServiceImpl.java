package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.inventory.dtos.CreateInventory;
import com.techpeak.hac.inventory.dtos.InventoryResponse;
import com.techpeak.hac.inventory.dtos.InventoryShortResponse;
import com.techpeak.hac.inventory.mappers.InventoryMapper;
import com.techpeak.hac.inventory.models.Inventory;
import com.techpeak.hac.inventory.models.Product;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.inventory.models.StoreLocation;
import com.techpeak.hac.inventory.repositories.InventoryRepository;
import com.techpeak.hac.inventory.services.InventoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.StoreLocationService;
import com.techpeak.hac.inventory.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final StoreService storeService;
    private final StoreLocationService storeLocationService;
    private final ProductService productService;

    @Override
    public Page<InventoryResponse> listWithPages(Pageable pageRequest, String productNumber) {
        Page<Inventory> inventory = inventoryRepository.findByProductNumberContainingIgnoreCase(productNumber, pageRequest);
        return inventory.map(InventoryMapper::toResponse);
    }

    @Override
    public void create(CreateInventory createInventory) {
//        try {
        Product product = productService.getProductOrThrow(createInventory.productId());
        Store store = storeService.getOrElseThrow(createInventory.storeId());

        Inventory inventory;
        if (createInventory.locationId() != null) {
            StoreLocation storeLocation = storeLocationService.getOrElseThrow(createInventory.locationId());
            inventory = Inventory.builder()
                    .product(product)
                    .store(store)
                    .location(storeLocation)
                    .quantity(createInventory.quantity())
                    .build();
            inventoryRepository.save(inventory);
            return;
        }
        inventory = Inventory.builder()
                .product(product)
                .store(store)
                .quantity(createInventory.quantity())
                .build();
        inventoryRepository.save(inventory);
//        } catch (DataIntegrityViolationException e) {
//            throw new DuplicateRecordException("Duplicate inventory record");
//        }
    }

    @Override
    public void create(Product product, Store store, Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        Optional<Inventory> inventory =
                inventoryRepository.findByProductIdAndStoreId(product.getId()
                        , store.getId());
        if (inventory.isPresent()) {
            inventory.get().setReservedQuantity(inventory.get().getReservedQuantity() + quantity);
            inventoryRepository.save(inventory.get());
        } else {
            Inventory inventory1 = Inventory.builder()
                    .product(product)
                    .store(store)
                    .quantity(0)
                    .reservedQuantity(quantity)
                    .build();
            inventoryRepository.save(inventory1);
        }
    }

    @Override
    public void updateReservedQuantity(Product product, Store store, Integer quantity) {
        Optional<Inventory> inventory =
                inventoryRepository.findByProductIdAndStoreId(product.getId()
                        , store.getId());
        if (inventory.isPresent()) {

            inventory.get().setReservedQuantity(inventory.get().getReservedQuantity() - quantity);
            inventoryRepository.save(inventory.get());
        } else {
            throw new NotFoundException("Inventory not found with product id " + product.getId() + " and store id " + store.getId());
        }
    }


    @Override
    public List<InventoryShortResponse> getByProductId(Long id) {
//        return inventoryRepository.findAllByProductShort(id);
        List<InventoryShortResponse> allByProductShort = inventoryRepository.findAllByProductShort(id);
//        List<InventoryShortResponse>  all =  allByProductShort.stream().map((i)->{
//            return new InventoryShortResponse(
//                    (Long) i.get("id"),
//                    (Integer) i.get("quantity"),
//                    (String) i.get("name_ar"),
//                    (String) i.get("name_en"),
//                    (String) i.get("name_ar"),
//                    (String) i.get("name_en"));
//        }).toList();
//
        return allByProductShort;
    }


}
