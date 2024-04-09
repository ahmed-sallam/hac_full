package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateMachinePart;
import com.techpeak.hac.inventory.dtos.MachinePartResponse;
import com.techpeak.hac.inventory.dtos.UpdateMachinePart;
import com.techpeak.hac.inventory.models.MachinePart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MachinePartService {
    void create(CreateMachinePart createMachinePart);
    void update(Long id, UpdateMachinePart updateMachinePart);
    Page<MachinePartResponse> list (Pageable pageable, Boolean isActive, String name);

    MachinePart getMachinePartOrThrow(Long id);
    MachinePartResponse get(Long id);
}
