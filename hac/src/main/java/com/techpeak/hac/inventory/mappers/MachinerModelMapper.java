package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.*;
import com.techpeak.hac.inventory.models.MachineryModel;
import com.techpeak.hac.inventory.models.StoreLocation;

public class MachinerModelMapper {
    public static MachineryModelResponse toMachineryModelResponse(MachineryModel machineryModel) {
        MachineryModelResponse machineryModelResponse = new MachineryModelResponse();
        machineryModelResponse.setId(machineryModel.getId());
        machineryModelResponse.setNameAr(machineryModel.getNameAr());
        machineryModelResponse.setNameEn(machineryModel.getNameEn());
        machineryModelResponse.setMachineryType(machineryModel.getMachineryType().getId());
        machineryModelResponse.setBrand(BrandMapper.toDtoWithoutSubBrands(machineryModel.getBrand()));
        machineryModelResponse.setIsActive(machineryModel.getIsActive());
        machineryModelResponse.setCreatedAt(machineryModel.getCreatedAt());
        machineryModelResponse.setUpdatedAt(machineryModel.getUpdatedAt());
        return machineryModelResponse;
    }

    public static MachineryModel toMachineryModel(CreateMachineryModel createMachineryModel) {
        MachineryModel machineryModel = new MachineryModel();
        machineryModel.setNameAr(createMachineryModel.getNameAr());
        machineryModel.setNameEn(createMachineryModel.getNameEn());
        machineryModel.setIsActive(createMachineryModel.getIsActive());
        return machineryModel;
    }
}
