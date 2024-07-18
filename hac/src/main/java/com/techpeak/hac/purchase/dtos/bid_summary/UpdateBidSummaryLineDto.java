package com.techpeak.hac.purchase.dtos.bid_summary;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public record UpdateBidSummaryLineDto(
        Long id,
        @Positive Integer quantity,
        @NotNull Long quotationId,
        @NotNull Long productId,
        @NotNull Long supplierId,
        Double price,
        Double vat,
        Double total) implements Serializable {
}
