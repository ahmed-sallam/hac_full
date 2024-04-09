package com.techpeak.hac.inventory.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.inventory.models.Alternative}
 */
@AllArgsConstructor
@Getter
@ToString
public class CreateAlternative implements Serializable {
    @NotEmpty
    private final String product1Number;
    @NotEmpty
    private final String product2Number;
}