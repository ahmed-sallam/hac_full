package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.Related;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelatedRepository extends JpaRepository<Related, Long> {

    @Query("SELECT a FROM Related a WHERE LOWER(a.product1Number) LIKE %:productNumber% OR LOWER(a.product2Number) LIKE :productNumber")
    List<Related> findAllByProductNumberContainingIgnoreCase(@Param("productNumber") String productNumber);
}
