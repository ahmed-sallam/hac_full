package com.techpeak.hac.sales.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;

import lombok.Builder;

/**
 * DTO for {@link com.techpeak.hac.sales.models.SaleOrder}
 */
@Builder
public record SaleOrderResponseShort(
        Long id,
        Boolean isActive,
        RequestStatus status,
        String number,
        BigDecimal total,
        LocalDate date,
        PaymentTerms paymentTerms,
        InternalRefDto internalRef,
        UserDtoShort user,
        CustomerResponseName customer,
        BigDecimal discount,
        LocalDate deliveryDate // Added deliveryDate to match the entity
) implements Serializable {
}