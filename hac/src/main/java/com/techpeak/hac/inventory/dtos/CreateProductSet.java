package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.ProductSet}
 */
@Data
@AllArgsConstructor
public class CreateProductSet  {
    @NotNull
    @Min(1)
    @Positive
    private Integer quantity;
    @NotNull
    private Long productId;
    private Long productSetId;
}