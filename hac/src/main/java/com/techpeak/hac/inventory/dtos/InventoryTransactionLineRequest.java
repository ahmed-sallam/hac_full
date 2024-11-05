package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.InventoryTransactionLine}
 */
public record InventoryTransactionLineRequest(Long productId,
                                              @Positive Integer quantity,
                                              @NotNull @Positive BigDecimal cost) implements Serializable {
}
