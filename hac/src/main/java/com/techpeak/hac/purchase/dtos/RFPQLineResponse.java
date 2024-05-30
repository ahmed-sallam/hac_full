package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RFPQLineResponse {
    private Integer quantity;
    private String notes;
    private ProductResponseShort product;
}
