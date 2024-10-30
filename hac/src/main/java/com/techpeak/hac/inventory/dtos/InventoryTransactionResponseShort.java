package com.techpeak.hac.inventory.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InventoryTransactionResponseShort {
    private Long id;
    private String transactionType;
    private Integer quantity;
    private ProductResponseShort product;
    private StoreResponseShort store;
    private String location;
    private String desStore;
    private String desLocation;
    private String transactionDate;
}
