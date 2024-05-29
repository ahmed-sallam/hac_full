package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestLineWithStockDto implements Serializable {
    private Integer quantity;
    private String notes;
    private ProductResponseShort product;
    private int storeInventory;

}
