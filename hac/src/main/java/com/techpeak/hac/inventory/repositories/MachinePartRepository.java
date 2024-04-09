package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.MachinePart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachinePartRepository extends JpaRepository<MachinePart, Long>
{
     List<MachinePart> findByIsActive(Boolean isActive);
     Page<MachinePart> findByIsActive(Boolean isActive , Pageable pageable );

     @Query("SELECT m FROM MachinePart m WHERE m.isActive = :isActive AND (LOWER(m.nameAr) LIKE %:nameAr% OR LOWER(m.nameEn) LIKE %:nameEn%)")
     Page<MachinePart> findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(
             @Param("isActive") boolean isActive,
             @Param("nameAr") String nameAr,
             @Param("nameEn") String nameEn,
             Pageable pageable
     );

}
