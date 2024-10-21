package com.techpeak.hac.sales.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSaleOrderLine {

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    @NotNull(message = "Discount is required")
    @PositiveOrZero(message = "Discount must be zero or a positive number")
    private BigDecimal discount = BigDecimal.ZERO;

    @Size(max = 255, message = "Notes can be up to 255 characters long")
    private String notes;

    @NotNull(message = "Product is required")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;
}