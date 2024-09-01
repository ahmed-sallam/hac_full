package com.techpeak.hac.sales.repositories;

import com.techpeak.hac.sales.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c  WHERE c.isActive = :isActive AND  " +
            "(LOWER(c.nameAr) LIKE %:nameAr% OR LOWER(c.nameEn) LIKE " +
            "%:nameEn% )")
    Page<Customer> findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(
            @Param("isActive") boolean isActive,
            @Param("nameAr") String nameAr,
            @Param("nameEn") String nameEn,
            Pageable pageable
    );
}
