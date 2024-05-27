package com.techpeak.hac.purchase.repositories;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long> {
    Optional<MaterialRequest> findTopByOrderByNumberDesc();

    @Query("SELECT mr FROM MaterialRequest mr WHERE mr.status = :status")
    List<MaterialRequest> findByStatus(@Param("status") RequestStatus status);
    Page<MaterialRequest> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT mr FROM MaterialRequest mr WHERE mr.isActive = :isActive")
    List<MaterialRequest> findByIsActive(@Param("isActive") Boolean isActive);
    Page<MaterialRequest> findByIsActive(Boolean isActive, Pageable pageable);

    List<MaterialRequest> findByUser(User user);
    Page<MaterialRequest> findByUser(User user, Pageable pageable);

    @Query("SELECT COUNT(mr) FROM MaterialRequest mr WHERE mr.status = :status")
    Long countByStatus(@Param("status") RequestStatus status);
    @Modifying
    @Query("UPDATE MaterialRequest mr SET mr.status = :status WHERE mr.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") RequestStatus status);


}
