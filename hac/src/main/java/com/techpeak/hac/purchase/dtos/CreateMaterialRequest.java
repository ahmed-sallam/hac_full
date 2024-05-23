package com.techpeak.hac.purchase.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMaterialRequest {


    @Size(max = 255, message = "Notes cannot exceed 200 characters")
    private String notes;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date must be in the past or present")
    private LocalDate date;

    @NotNull(message = "Store ID cannot be null")
    @Positive(message = "Product ID must be a positive number")
    private Long store;


    @NotNull(message = "Lines cannot be null")
    @NotEmpty(message = "Lines cannot be empty")
    @Valid
    private Set<CreateMaterialRequestLine> lines;
}
