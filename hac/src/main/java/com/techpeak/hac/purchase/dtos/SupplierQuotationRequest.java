package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.ReceiveTypes;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotation}
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierQuotationRequest implements Serializable {
    String notes;
    @NotNull
    @FutureOrPresent
    LocalDate date;
    @NotNull
    @FutureOrPresent
    LocalDate validTo;
    @NotNull
    ReceiveTypes receiveIn;
    Long currencyId;
    @PositiveOrZero
    Double subTotal;
    Double discount;
    @PositiveOrZero
    Double vat;
    @PositiveOrZero
    Double totalExpenses;
    @PositiveOrZero
    Double total;
    @NotNull
    Boolean isLocal;
    @NotNull
    PaymentTerms paymentTerms;
    String supplierRef;
    Long internalRefId;
    Long userId;
    Long rfpqId;
    Long supplierId;
    Set<SupplierQuotationExpensesRequest> expenses;
    Set<SupplierQuotationLineRequest> lines;
}