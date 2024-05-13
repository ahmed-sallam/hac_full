package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateMachineryType;
import com.techpeak.hac.inventory.dtos.CreateStore;
import com.techpeak.hac.inventory.dtos.MachineryTypeResponse;
import com.techpeak.hac.inventory.dtos.StoreResponse;
import com.techpeak.hac.inventory.models.MachineryType;
import com.techpeak.hac.inventory.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MachineryTypeService {
void create(CreateMachineryType createMachineryType);
void update(Long id, CreateMachineryType createMachineryType);
Page<MachineryTypeResponse> list(Boolean isActive, String name, Pageable pageable);
MachineryType getOrElseThrow(Long id);
MachineryTypeResponse getMachineryType(Long id);
}
