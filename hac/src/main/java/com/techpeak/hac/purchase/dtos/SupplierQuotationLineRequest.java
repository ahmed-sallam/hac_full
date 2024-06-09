package com.techpeak.hac.purchase.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotationLine}
 */
@Value
public class SupplierQuotationLineRequest implements Serializable {
    @Positive
    Integer quantity;
    @PositiveOrZero
    Double price;
    @PositiveOrZero
    Double discount;
//    @PositiveOrZero
//    Double vat;
//    @PositiveOrZero
//    Double subTotal;
    @PositiveOrZero
    Double total;
    String notes;
    Long productId;
}