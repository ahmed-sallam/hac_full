package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT s FROM Store s WHERE s.isActive = :isActive AND s.isVirtual = false AND (LOWER(s.nameAr) LIKE %:nameAr% OR LOWER(s.nameEn) LIKE %:nameEn%)")
    Page<Store> findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("nameAr") String nameAr,
            @Param("nameEn") String nameEn,
            Pageable pageable
    );
}
