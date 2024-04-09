package com.techpeak.hac.inventory.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductSetItemResponse extends ProductResponse {

    private Integer quantity;
    private Long setId;
}