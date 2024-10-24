package com.techpeak.hac.sales.dtos;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class SaleOrderResponse implements Serializable {
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
    PaymentTerms paymentTerms;
    InternalRefDto internalRef;
    UserDtoShort user;
    CustomerResponse customer;
    Set<SaleOrderLineDto> lines;
    Set<UserHistoryResponse> userHistories;
    LocalDate deliveryDate;
    private StoreResponseShort store;
}
