package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.dtos.InventoryShortResponse;
import com.techpeak.hac.inventory.models.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p  WHERE  LOWER(p.productNumber) LIKE %:productNumber%  ORDER BY p.productNumber ASC ")
    Page<Inventory> findByProductNumberContainingIgnoreCase(
            @Param("productNumber") String productNumber,
            Pageable pageable
    );

    @Query("SELECT i, SUM(i.quantity) as totalInventory FROM Inventory i JOIN i.product p WHERE  LOWER(p.productNumber) LIKE %:productNumber% GROUP BY i.quantity ORDER BY p.mainBrand.nameEn ASC, p.subBrand.nameEn ASC")
    Page<Inventory> findByProductNumberContainingIgnoreCaseGroupByProduct(
            @Param("productNumber") String productNumber,
            Pageable pageable
    ); // todo complete this and use it as main query to send data ready to frontend.


    @Query("SELECT NEW com.techpeak.hac.inventory.dtos.InventoryShortResponse(i.id, i.quantity, i.store.nameAr, i.store.nameEn,  COALESCE(l.nameAr, ''), COALESCE(l.nameEn, ''), i.store.id, i.location.id) FROM Inventory i LEFT JOIN i.location l WHERE i.product.id = :id")
    List<InventoryShortResponse> findAllByProductShort(@Param("id") Long id);


    @Query("SELECT i FROM Inventory i WHERE i.product.id = :id")
    Optional<Inventory> findByProductId(@Param("id") Long id);

    // get  one inventory by product id and store id
    Optional<Inventory> findByProductIdAndStoreId(Long productId, Long storeId);
}
