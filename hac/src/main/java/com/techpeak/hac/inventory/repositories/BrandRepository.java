package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b  WHERE b.isActive = :isActive AND b.mainBrand IS NULL AND (LOWER(b.nameAr) LIKE %:nameAr% OR LOWER(b.nameEn) LIKE %:nameEn% OR LOWER(b.code) LIKE %:code%)")
    Page<Brand> findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCaseOrCodeContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("nameAr") String nameAr,
            @Param("nameEn") String nameEn,
            @Param("code") String code,
            Pageable pageable
    );
}
