package com.techpeak.hac.inventory.repositories;

import com.techpeak.hac.inventory.models.StoreLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreLocationRepository  extends JpaRepository<StoreLocation, Long> {
}
