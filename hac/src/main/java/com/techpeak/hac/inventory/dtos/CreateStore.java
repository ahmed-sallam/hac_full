package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateStore {
    @NotEmpty
    @Max(25)
    private String nameAr;
    @NotEmpty
    @Max(25)
    private String nameEn;
    @NotEmpty
    @Max(25)
    private String cityAr;
    @NotEmpty
    @Max(25)
    private String cityEn;
    private Boolean isActive;
}
