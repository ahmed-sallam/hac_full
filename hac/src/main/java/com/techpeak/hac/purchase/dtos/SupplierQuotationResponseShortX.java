package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.purchase.enums.PaymentTerms;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotation}
 */
@Builder
public record SupplierQuotationResponseShortX(
        Long id,
        LocalDate date,
        CurrencyCodeResponse currency,
        Boolean isLocal,
        PaymentTerms paymentTerms
) implements Serializable {
}