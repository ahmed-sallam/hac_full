package com.techpeak.hac.sales.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link com.techpeak.hac.sales.models.SaleOrderLine}
 */
@Value
@Builder
public class SaleOrderLineDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    Integer quantity;
    BigDecimal price;
    BigDecimal discount;
    BigDecimal total;
    String notes;
    ProductResponseShort product;
}