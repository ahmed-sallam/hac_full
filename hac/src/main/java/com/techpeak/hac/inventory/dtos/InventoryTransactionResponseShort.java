package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.core.dtos.UserDtoShort;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InventoryTransactionResponseShort {
    private Long id;
    private String number;
    private Long internalRef;
    private String currentPhase;
    private String transactionType;
    private String transactionDate;
    private StoreResponseShort store;
    private StoreResponseShort desStore;
    private String status;
    private UserDtoShort user;
}
