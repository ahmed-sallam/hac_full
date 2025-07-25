package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.ReceiveTypes;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotation}
 */
@Getter
@Setter
@Builder
public class SupplierQuotationResponse implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    String notes;
    LocalDate date;
    LocalDate validTo;
    ReceiveTypes receiveIn;
    CurrencyCodeDto currency;
    Double subTotal;
    Double discount;
    Double vat;
    Double totalExpenses;
    Double total;
    Boolean isLocal;
    PaymentTerms paymentTerms;
    String supplierRef;
    InternalRefDto internalRef;
    UserDtoShort user;
    RFPQResponseNumber rfpq;
    SupplierResponseName supplier;
    Set<SupplierQuotationExpensesDto> expenses;
    Set<SupplierQuotationLineDto> lines;
    Set<UserHistoryResponse> userHistories;


    // public void setUserHistories(Set<UserHistoryResponse> userHistories) {
    // for (UserHistoryResponse userHistory : userHistories) {
    // userHistory.setRecordId(this.id);
    // }
    // }
}
