package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UpdateMachinePart extends CreateMachinePart{
  private final Boolean isActive;

    public UpdateMachinePart(@NotEmpty @Max(20) String nameAr, @NotEmpty @Max(20) String nameEn, Boolean isActive) {
        super(nameAr, nameEn);
        this.isActive = isActive;
    }
}
