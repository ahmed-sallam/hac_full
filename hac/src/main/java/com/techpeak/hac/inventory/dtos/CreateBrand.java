package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateBrand {
    @NotEmpty
    @Max(20)
    private String nameAr;
    @NotEmpty
    @Max(20)
    private String nameEn;
    @NotEmpty
    @Max(10)
    private String code;
    private Long brandId;
    private Boolean isActive;
}
