package com.techpeak.hac.inventory.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.inventory.dtos.CreateMachineryModel;
import com.techpeak.hac.inventory.dtos.MachineryModelResponse;
import com.techpeak.hac.inventory.mappers.MachinerModelMapper;
import com.techpeak.hac.inventory.models.MachineryModel;
import com.techpeak.hac.inventory.repositories.MachinerModelRepository;
import com.techpeak.hac.inventory.services.BrandService;
import com.techpeak.hac.inventory.services.MachineryModelService;
import com.techpeak.hac.inventory.services.MachineryTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MachineryModelServiceImpl implements MachineryModelService {
    private final MachinerModelRepository machinerModelRepository;
    private final MachineryTypeService machineryTypeService;
    private final BrandService brandService;

    @Override
    public void create(Long machineryTypeId, CreateMachineryModel createMachineryModel) {
        MachineryModel machineryModel = MachinerModelMapper.toMachineryModel(createMachineryModel);
        machineryModel.setMachineryType(machineryTypeService.getOrElseThrow(machineryTypeId));
        machineryModel.setBrand(brandService.getBrandOrThrow(createMachineryModel.getBrand()));
        machinerModelRepository.save(machineryModel);
    }

    @Override
    public void update(Long id, CreateMachineryModel createMachineryModel) {
        MachineryModel machineryModel = getOrElseThrow(id);
        machineryModel.setNameAr(createMachineryModel.getNameAr());
        machineryModel.setNameEn(createMachineryModel.getNameEn());
        machineryModel.setIsActive(createMachineryModel.getIsActive());
        machineryModel.setBrand(brandService.getBrandOrThrow(createMachineryModel.getBrand()));
        machinerModelRepository.save(machineryModel);
    }

    @Override
    public List<MachineryModelResponse> list() {
        return machinerModelRepository.findAll().stream()
                .map(MachinerModelMapper::toMachineryModelResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MachineryModel getOrElseThrow(Long id) {
        return machinerModelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Machinery Model with id " + id + " does not exist"));
    }
}
