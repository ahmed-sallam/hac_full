package com.techpeak.hac.purchase.dtos.bid_summary;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public record CreateBidSummaryLineDto(@Positive Integer quantity,
                                      @NotNull Long quotationId,
                                      @NotNull Long productId,
                                      @NotNull Long supplierId) implements Serializable {
}