package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.inventory.dtos.CreateMachineryType;
import com.techpeak.hac.inventory.dtos.MachineryTypeResponse;
import com.techpeak.hac.inventory.mappers.MachineryTypeMapper;
import com.techpeak.hac.inventory.models.MachineryType;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.inventory.repositories.MachineryTypeRepository;
import com.techpeak.hac.inventory.services.MachineryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MachineryTypeServiceImpl implements MachineryTypeService {
    private final MachineryTypeRepository machineryTypeRepository;

    @Override
    public void create(CreateMachineryType createMachineryType) {
        MachineryType machineryType = MachineryTypeMapper.toEntity(createMachineryType);
        machineryTypeRepository.save(machineryType);
    }

    @Override
    public void update(Long id, CreateMachineryType createMachineryType) {
        MachineryType machineryType = getOrElseThrow(id);
        machineryType.setNameAr(createMachineryType.getNameAr());
        machineryType.setNameEn(createMachineryType.getNameEn());
        machineryType.setIsActive(createMachineryType.getIsActive());
        machineryTypeRepository.save(machineryType);
    }

    @Override
    public Page<MachineryTypeResponse> list(Boolean isActive, String name, Pageable pageable) {
        Page<MachineryType> machineryTypePage = machineryTypeRepository
                .findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(isActive, name, name, pageable);
        return machineryTypePage.map(MachineryTypeMapper::toDto);
    }

    @Override
    public MachineryType getOrElseThrow(Long id) {
        return machineryTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Machinery Type with id " + id + " does not exist"));
    }

    @Override
    public MachineryTypeResponse getMachineryType(Long id) {
        MachineryType machineryType = getOrElseThrow(id);
        MachineryTypeResponse machineryTypeResponse = MachineryTypeMapper.toDto(machineryType);
        return machineryTypeResponse;
    }
}
