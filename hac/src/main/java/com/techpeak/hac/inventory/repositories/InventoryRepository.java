package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.dtos.InventoryShortResponse;
import com.techpeak.hac.inventory.models.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    @Query("SELECT i FROM Inventory i  WHERE  LOWER(i.product.productNumber) LIKE %:productNumber% ")
    Page<Inventory> findByProductNumberContainingIgnoreCase(
            @Param("productNumber") String productNumber,
            Pageable pageable
    );

//    @Query("SELECT i FROM Inventory i  WHERE i.product.id = :id")
//    List<Inventory> findAllByProduct( @Param("id") Long id);

    @Query("SELECT NEW com.techpeak.hac.inventory.dtos.InventoryShortResponse(i.id, i.quantity, i.store.nameAr, i.store.nameEn,  COALESCE(l.nameAr, ''), COALESCE(l.nameEn, '')) FROM Inventory i LEFT JOIN i.location l WHERE i.product.id = :id")
    List<InventoryShortResponse> findAllByProductShort(@Param("id") Long id);
}
