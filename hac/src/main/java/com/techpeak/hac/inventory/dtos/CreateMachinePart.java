package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMachinePart {
    @NotEmpty
    @Max(20)
    private final String nameAr;
    @NotEmpty
    @Max(20)
    private final String nameEn;
}
