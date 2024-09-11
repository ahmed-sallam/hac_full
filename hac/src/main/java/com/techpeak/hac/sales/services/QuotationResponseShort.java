package com.techpeak.hac.sales.services;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.purchase.dtos.RFPQResponseNumber;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.sales.dtos.CustomerResponseName;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.techpeak.hac.sales.models.Quotation}
 */
@Builder
public record QuotationResponseShort(Long id, Boolean isActive,
                                     RequestStatus status, String number,
                                     Double total, LocalDate date,
                                     PaymentTerms paymentTerms,
                                     String internalRef,
                                     UserDtoShort user,
                                     RFPQResponseNumber rfpq,
                                     CustomerResponseName customer) implements Serializable {
}
