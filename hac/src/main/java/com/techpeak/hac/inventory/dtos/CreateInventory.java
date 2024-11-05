package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.Inventory}
 *
 * @param locationId this is optional
 */

public record CreateInventory(@NotNull @Min(0) @NotNull Integer quantity,
                              @NotNull Long productId,
                              @NotNull Long storeId,
                              Long locationId) implements Serializable {
}
