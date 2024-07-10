package com.techpeak.hac.purchase.dtos.purchase_order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseOrderLine implements Serializable {
    @Positive
    private Integer quantity;
    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Double vat;
    @PositiveOrZero
    private Double total;
    private String notes;
    @NotNull
    private Long productId;
}