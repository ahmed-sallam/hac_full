package com.techpeak.hac.sales.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSaleOrder implements Serializable {

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;

    @NotNull(message = "Order Date cannot be null")
    @PastOrPresent(message = "Order Date must be in the past or present")
    private LocalDate date = LocalDate.now();

    @NotNull(message = "Discount is required")
    @PositiveOrZero(message = "Discount must be zero or a positive number")
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be a positive number")
    private Long customer;

    @NotNull(message = "Currency ID cannot be null")
    @Positive(message = "Currency ID must be a positive number")
    private Long currency;

    @NotNull(message = "Lines cannot be null")
    @NotEmpty(message = "Lines cannot be empty")
    @Valid
    private Set<CreateSaleOrderLine> lines;

    private PaymentTerms paymentTerms = PaymentTerms.IMMEDIATELY;

    private RequestStatus status = RequestStatus.DRAFT;

    @NotNull(message = "Delivery Date cannot be null")
    private LocalDate deliveryDate;
}