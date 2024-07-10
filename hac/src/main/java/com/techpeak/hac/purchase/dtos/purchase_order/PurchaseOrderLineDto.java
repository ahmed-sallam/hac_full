package com.techpeak.hac.purchase.dtos.purchase_order;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderLineDto implements Serializable{
    private Long id;
    private Integer quantity;
    private Double price;
    private Double vat;
    private Double total;
    private String notes;
    private ProductResponseShort product;
}
