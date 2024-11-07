package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long>, JpaSpecificationExecutor<InventoryTransaction> {
    Optional<InventoryTransaction> findTopByOrderByNumberDesc();

    Optional<InventoryTransaction> findByInternalRefId(Long id);

    @Query("select it, uh from InventoryTransaction it left join fetch it.lines left join " +
            "UserHistory uh on (uh.recordId = it.id and uh.tableName = 'inventory_transactions') where it.id = :id")
    List<Object[]> findByIdWithLines(@Param("id") Long id);

}
