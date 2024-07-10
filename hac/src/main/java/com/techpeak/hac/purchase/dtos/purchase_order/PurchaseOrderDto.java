package com.techpeak.hac.purchase.dtos.purchase_order;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import com.techpeak.hac.purchase.dtos.SupplierResponseName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderDto implements Serializable {
    private Long id;
    private LocalDate date;
    private String notes;
    private Long internalRefId;
    private String currentPhase;
    private String number;
    private Double subTotal;
    private Double vat;
    private Double total;
    private String status;
    private StoreResponseShort store;
    private UserDtoShort user;
    private SupplierResponseName supplier;
    private Set<PurchaseOrderLineDto> lines;
}