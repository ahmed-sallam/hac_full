package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    Optional<InventoryTransaction> findTopByOrderByNumberDesc();

    Optional<InventoryTransaction> findByInternalRefId(Long id);

}
