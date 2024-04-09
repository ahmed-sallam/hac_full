package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.core.exception.DuplicateRecordException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void create(CreateInventory createInventory) throws Exception {
        try {
            System.out.println("ffff " + createInventory);
            Product product = productService.getProductOrThrow(createInventory.productId());
            Store store = storeService.getOrElseThrow(createInventory.storeId());
            Inventory inventory;
            if (createInventory.locationId() != null) {
                StoreLocation storeLocation = storeLocationService.getOrElseThrow(createInventory.locationId());
                inventory = new Inventory(createInventory.quantity(), product, store, storeLocation);
                inventoryRepository.save(inventory);
                return;
            }
            inventory = new Inventory(createInventory.quantity(), product, store, null);
            inventoryRepository.save(inventory);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Errorrrrrrr " + e);
            throw new DuplicateRecordException("Duplicate inventory record");
        }
    }

    @Override
    public List<InventoryShortResponse> getByProductId(Long id) {
//        return inventoryRepository.findAllByProductShort(id);
        List<InventoryShortResponse> allByProductShort = inventoryRepository.findAllByProductShort(id);
//        List<InventoryShortResponse>  all =  allByProductShort.stream().map((i)->{
//            System.out.println("aaaa" + i.toString());
//            return new InventoryShortResponse(
//                    (Long) i.get("id"),
//                    (Integer) i.get("quantity"),
//                    (String) i.get("name_ar"),
//                    (String) i.get("name_en"),
//                    (String) i.get("name_ar"),
//                    (String) i.get("name_en"));
//        }).toList();
//
//        System.out.println("aaa "+ allByProductShort);
        return allByProductShort;
    }


}
