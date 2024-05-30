package com.techpeak.hac.purchase.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRFPQLine {
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 255, message = "Notes can be up to 255 characters long")
    private String notes;

    @NotNull(message = "Product is required")
    @Positive(message = "Product ID must be a positive number")
    private Long product;
}
