package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.CreateMachineryType;
import com.techpeak.hac.inventory.dtos.CreateStore;
import com.techpeak.hac.inventory.dtos.MachineryTypeResponse;
import com.techpeak.hac.inventory.dtos.StoreResponse;
import com.techpeak.hac.inventory.models.MachineryModel;
import com.techpeak.hac.inventory.models.MachineryType;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.inventory.models.StoreLocation;

import java.util.Set;

public class MachineryTypeMapper {
    public static MachineryType toEntity(CreateMachineryType createMachineryType) {
        MachineryType machineryType =  MachineryType.builder()
                .nameAr(createMachineryType.getNameAr())
                .nameEn(createMachineryType.getNameEn())
                        .build();
        machineryType.setIsActive(createMachineryType.getIsActive());
        return machineryType;
    }

    public static MachineryTypeResponse toDto(MachineryType machineryType){
        MachineryTypeResponse machineryTypeResponse = new MachineryTypeResponse(
        );
        machineryTypeResponse.setId(machineryType.getId());
        machineryTypeResponse.setNameAr(machineryType.getNameAr());
        machineryTypeResponse.setNameEn(machineryType.getNameEn());
        machineryTypeResponse.setIsActive(machineryType.getIsActive());
        machineryTypeResponse.setCreatedAt(machineryType.getCreatedAt());
        machineryTypeResponse.setUpdatedAt(machineryType.getUpdatedAt());
        Set<MachineryModel> machineryModels = machineryType.getMachineryModels();
        machineryTypeResponse.setMachineryModels(machineryModels.stream()
                .map(MachinerModelMapper::toMachineryModelResponse)
                .collect(java.util.stream.Collectors.toList()));
        return machineryTypeResponse;
    }
}
