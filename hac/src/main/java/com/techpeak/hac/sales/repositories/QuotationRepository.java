package com.techpeak.hac.sales.repositories;

import com.techpeak.hac.sales.models.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, Long>,
        JpaSpecificationExecutor<Quotation> {

    Optional<Quotation> findTopByOrderByNumberDesc();

    @Query("select q , uh from Quotation q left join fetch q.lines left join " +
            "UserHistory  uh on (uh.recordId = q.id and uh.tableName = 'quotations')  where q.id = :id")
    List<Object[]> findByIdWithLines(@Param("id") Long id);
}
