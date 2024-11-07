package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long>, JpaSpecificationExecutor<InventoryTransaction> {
    Optional<InventoryTransaction> findTopByOrderByNumberDesc();

    Optional<InventoryTransaction> findByInternalRefId(Long id);

}
