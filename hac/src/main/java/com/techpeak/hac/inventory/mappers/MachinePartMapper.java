package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.CreateMachinePart;
import com.techpeak.hac.inventory.dtos.MachinePartResponse;
import com.techpeak.hac.inventory.models.MachinePart;

public class MachinePartMapper {

    public static MachinePart toEntity(CreateMachinePart createMachinePart) {
        MachinePart machinePart =  MachinePart.builder()
                .nameAr(createMachinePart.getNameAr())
                .nameEn(createMachinePart.getNameEn())
                        .build();
//        machinePart.setIsActive(createMachinePart.getIsActive());
        return machinePart;
    }

    public static MachinePartResponse toDto(MachinePart machinePart){
        return new MachinePartResponse(
                machinePart.getId(),
                machinePart.getNameAr(),
                machinePart.getNameEn(),
                machinePart.getIsActive(),
                machinePart.getCreatedAt(),
                machinePart.getUpdatedAt()
        );
    }

}
