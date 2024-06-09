package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.core.dtos.InternalRefDto;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotation}
 */
@Builder
public record SupplierQuotationResponseShort(Long id, Boolean isActive,
                                             LocalDate date,
                                             CurrencyCodeDto currency,
                                             Double total, Boolean isLocal,
                                             PaymentTerms paymentTerms,
                                             String supplierRef,
                                             InternalRefDto internalRef,
                                             UserDtoShort user,
                                             RFPQResponseNumber rfpq,
                                             SupplierResponseName supplier) implements Serializable {
}