package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotationLine}
 */
@Value
@Builder
public class SupplierQuotationLineDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    Integer quantity;
    Double price;
    Double discount;
    Double total;
    String notes;
    ProductResponseShort product;
}