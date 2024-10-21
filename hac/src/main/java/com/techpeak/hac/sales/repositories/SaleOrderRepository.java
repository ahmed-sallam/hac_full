package com.techpeak.hac.sales.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techpeak.hac.sales.models.SaleOrder;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long>, JpaSpecificationExecutor<SaleOrder> {

    Optional<SaleOrder> findTopByOrderByNumberDesc();

    @Query("select so, uh from SaleOrder so left join fetch so.lines left join " +
            "UserHistory uh on (uh.recordId = so.id and uh.tableName = 'sale_orders') where so.id = :id")
    List<Object[]> findByIdWithLines(@Param("id") Long id);
}