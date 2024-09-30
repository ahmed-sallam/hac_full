package com.techpeak.hac.sales.dtos;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.purchase.dtos.RFPQResponseNumber;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder
public class QuotationResponse implements Serializable {
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
    LocalDate date;
    LocalDate validUntil;
    PaymentTerms paymentTerms;
    InternalRefDto internalRef;
    UserDtoShort user;
    RFPQResponseNumber rfpq;
    CustomerResponse customer;
    Set<QuotationLineDto> lines;
    Set<UserHistoryResponse> userHistories;
}
