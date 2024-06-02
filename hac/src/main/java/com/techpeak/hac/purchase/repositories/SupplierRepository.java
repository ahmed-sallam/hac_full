package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.purchase.models.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s  WHERE s.isActive = :isActive AND  (LOWER(s.nameAr) LIKE %:nameAr% OR LOWER(s.nameEn) LIKE %:nameEn% )")
    Page<Supplier> findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("nameAr") String nameAr,
            @Param("nameEn") String nameEn,
            Pageable pageable
    );

}
