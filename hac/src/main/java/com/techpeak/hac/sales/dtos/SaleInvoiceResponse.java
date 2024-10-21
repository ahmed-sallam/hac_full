package com.techpeak.hac.sales.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SaleInvoiceResponse implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    RequestStatus status;
    String number;
    BigDecimal subTotal;
    BigDecimal discount;
    BigDecimal vat;
    BigDecimal total;
    String notes;
    LocalDate date; // Changed from invoiceDate to date
    PaymentTerms paymentTerms;
    InternalRefDto internalRef;
    UserDtoShort user;
    CustomerResponse customer;
    Set<SaleInvoiceLineDto> lines;
    Set<UserHistoryResponse> userHistories;
    LocalDate dueDate; // Added dueDate to match the entity
}