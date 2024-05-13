package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p,  SUM(i.quantity) AS totalInventory  FROM Product p LEFT JOIN Inventory i ON p.id = i.product.id  WHERE p.isActive = :isActive AND  LOWER(p.productNumber) LIKE %:productNumber%  GROUP BY p.id")
    Page<Object[]> findByIsActiveAndProductNumberContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("productNumber") String productNumber,
            Pageable pageable
    );
    @Query("SELECT p FROM Product p  WHERE p.isActive = :isActive AND  LOWER(p.productNumber) LIKE :productNumber ")
    List<Product> findByIsActiveAndProductNumberContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("productNumber") String productNumber
    );
    @Query("SELECT p ,  SUM(i.quantity) AS totalInventory   FROM Product p LEFT JOIN Inventory i ON p.id = i.product.id WHERE p.isActive = :isActive AND  LOWER(p.productNumber) LIKE :productNumber GROUP BY p.id")
    List<Object[]> findByIsActiveAndProductNumberContainingIgnoreCaseWithTotalInventory(
            @Param("isActive") boolean isActive,
            @Param("productNumber") String productNumber
    );

    @Query("SELECT p FROM Product p  WHERE p.isActive = :isActive AND  LOWER(p.productNumber) LIKE %:productNumber% ")
    List<Product> searchByProductNumber(
            @Param("isActive") boolean isActive,
            @Param("productNumber") String productNumber
    );
    @Query("SELECT p, SUM(i.quantity) AS totalInventory FROM Product p LEFT JOIN Inventory i ON p.id = i.product.id WHERE p.id = :productId GROUP BY p.id")
    List<Object[]> findProductWithTotalInventory(@Param("productId") Long productId);



//    @Query("SELECT p FROM Product p  JOIN FETCH p.setItems WHERE p.id = :id ")
//    Optional<Product> findById(Long id);
}
