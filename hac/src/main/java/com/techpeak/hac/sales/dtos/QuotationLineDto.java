package com.techpeak.hac.sales.dtos;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.sales.models.QuotationLine}
 */
@Value
@Builder
public class QuotationLineDto implements Serializable {
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
