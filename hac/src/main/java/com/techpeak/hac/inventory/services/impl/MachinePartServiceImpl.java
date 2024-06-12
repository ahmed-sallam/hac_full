package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.inventory.dtos.CreateMachinePart;
import com.techpeak.hac.inventory.dtos.MachinePartResponse;
import com.techpeak.hac.inventory.dtos.UpdateMachinePart;
import com.techpeak.hac.inventory.mappers.MachinePartMapper;
import com.techpeak.hac.inventory.models.MachinePart;
import com.techpeak.hac.inventory.repositories.MachinePartRepository;
import com.techpeak.hac.inventory.services.MachinePartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MachinePartServiceImpl implements MachinePartService {
    private final MachinePartRepository machinePartRepository;

    @Override
    public void create(CreateMachinePart createMachinePart) {
        MachinePart machinePart = MachinePartMapper.toEntity(createMachinePart);
        machinePartRepository.save(machinePart);
    }

    @Override
    public void update(Long id, UpdateMachinePart updateMachinePart) {
        MachinePart machinePart = getMachinePartOrThrow(id);
        machinePart.setNameAr(updateMachinePart.getNameAr());
        machinePart.setNameEn(updateMachinePart.getNameEn());
        machinePart.setIsActive(updateMachinePart.getIsActive());
        machinePartRepository.save(machinePart);
    }


    @Override
    public Page<MachinePartResponse> list(Pageable pageable, Boolean isActive, String name) {
        Page<MachinePart> all = machinePartRepository.findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(isActive, name, name, pageable);
        return all.map(MachinePartMapper::toDto);
    }

    @Override
    public MachinePart getMachinePartOrThrow(Long id) {
        return machinePartRepository.findById(id).orElseThrow(() -> new NotFoundException("MachinePart with id " + id + " does not exist"));
    }

    @Override
    public MachinePartResponse get(Long id) {
        MachinePart machinePart = getMachinePartOrThrow(id);
        return MachinePartMapper.toDto(machinePart);
    }
}
