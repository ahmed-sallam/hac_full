package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateMachineryType {
    @NotEmpty
    @Max(25)
    private String nameAr;
    @NotEmpty
    @Max(25)
    private String nameEn;
    private Boolean isActive = true;  // this not required when create but required when edit
}
