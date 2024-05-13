package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateMachineryModel {
    @NotEmpty
    @Max(20)
    private String nameAr;
    @NotEmpty
    @Max(20)
    private String nameEn;
    @NotEmpty
    private Long machineryType;
    @NotEmpty
    private Long brand;
    private Boolean isActive;
}
