package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.Objects;

@Getter

public class UpdateMachinePart extends CreateMachinePart{
  private final Boolean isActive;

    public UpdateMachinePart(@NotEmpty @Max(20) String nameAr, @NotEmpty @Max(20) String nameEn, Boolean isActive) {
        super(nameAr, nameEn);
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UpdateMachinePart that = (UpdateMachinePart) o;
        return Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isActive);
    }
}
