package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.ProductSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSetRepository extends JpaRepository<ProductSet, Long> {
    List<ProductSet> findAllByIsActive(boolean isActive);
}
