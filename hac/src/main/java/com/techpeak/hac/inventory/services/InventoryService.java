package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateInventory;
import com.techpeak.hac.inventory.dtos.InventoryResponse;
import com.techpeak.hac.inventory.dtos.InventoryShortResponse;
import com.techpeak.hac.inventory.models.Product;
import com.techpeak.hac.inventory.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    Page<InventoryResponse> listWithPages(Pageable pageRequest, String productNumber);

    void create(CreateInventory createInventory);

    void create(Product product, Store store, Integer quantity);

    void updateReservedQuantity(Product product, Store store,
                                Integer quantity);

    List<InventoryShortResponse> getByProductId(Long id);
}
