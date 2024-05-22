package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.Alternative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlternativesRepository extends JpaRepository<Alternative, Long> {

    // generate query to find all alternatives by product1Number or product2Number and return all products that contain the given product numbers
    @Query("SELECT a FROM Alternative a WHERE LOWER(a.product1Number) LIKE %:productNumber% OR LOWER(a.product2Number) LIKE :productNumber")
    List<Alternative> findAllByProductNumberContainingIgnoreCase(@Param("productNumber") String productNumber);
}
