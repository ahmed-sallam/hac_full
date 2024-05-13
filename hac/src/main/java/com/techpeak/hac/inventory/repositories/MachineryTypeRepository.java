package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.MachineryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineryTypeRepository extends JpaRepository<MachineryType, Long> {
    @Query("SELECT s FROM MachineryType s WHERE s.isActive = :isActive AND (LOWER(s.nameAr) LIKE %:nameAr% OR LOWER(s.nameEn) LIKE %:nameEn%)")
    Page<MachineryType> findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("nameAr") String nameAr,
            @Param("nameEn") String nameEn,
            Pageable pageable
    );
}
