package com.techpeak.hac.purchase.dtos.purchase_order;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseOrder implements Serializable {
    @NotNull
    @FutureOrPresent
    private LocalDate date;
    private String status;
    @NotNull
    private Long storeIdId;
    private Long internalRefId;
    private Long userIdId;
    @NotNull
    private Long supplierId;
    @NotNull
    private Set<CreatePurchaseOrderLine> lines;
    @PositiveOrZero
    private Double subTotal;
    @PositiveOrZero
    private Double vat;
    @PositiveOrZero
    private Double total;
}