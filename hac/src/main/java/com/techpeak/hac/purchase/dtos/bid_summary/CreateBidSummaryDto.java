package com.techpeak.hac.purchase.dtos.bid_summary;

import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

public record CreateBidSummaryDto(@NotNull LocalDate fromDate,
                                  @NotNull Long rfpqId,
                                  @NotNull(message = "Lines cannot be null")
                                  @NotEmpty(message = "Lines cannot be empty")
                                  @Valid
                                  Set<CreateBidSummaryLineDto> lines,
                                  RequestStatus status) implements Serializable {
    public CreateBidSummaryDto {
        status = (status != null) ? status : RequestStatus.DRAFT;
    }

}