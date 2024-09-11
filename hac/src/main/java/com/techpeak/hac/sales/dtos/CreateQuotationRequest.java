package com.techpeak.hac.sales.dtos;

import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuotationRequest implements Serializable {

    @Size(max = 255, message = "Notes cannot exceed 200 characters")
    private String notes;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Date must be in the past or present")
    private LocalDate date;

    @NotNull(message = "Valid To cannot be null")
    @PastOrPresent(message = "Valid To must be in the past or present")
    private LocalDate validTo;

    @NotNull(message = "Store ID cannot be null")
    @Positive(message = "Store ID must be a positive number")
    private Long store;
    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be a positive number")
    private Long customer;
    @NotNull(message = "Currency ID cannot be null")
    @Positive(message = "Currency ID must be a positive number")
    private Long currency;


    @NotNull(message = "Lines cannot be null")
    @NotEmpty(message = "Lines cannot be empty")
    @Valid
    private Set<CreateQuotationRequestLine> lines;

    private PaymentTerms paymentTerms = PaymentTerms.IMMEDIATELY;

    private RequestStatus status = RequestStatus.DRAFT;

}
