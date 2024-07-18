package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.purchase.dtos.BidSummaryResponseShort;
import com.techpeak.hac.purchase.dtos.bid_summary.OneBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.OneBidSummaryLineDto;
import com.techpeak.hac.purchase.models.BidSummary;
import com.techpeak.hac.purchase.models.BidSummaryLine;

import java.util.Set;

public class BidSummaryMapper {
    private BidSummaryMapper() {
    }

    public static BidSummaryResponseShort mapToShortResponse(BidSummary item) {
        return BidSummaryResponseShort.builder()
                .id(item.getId())
                .createdAt(item.getCreatedAt())
                .number(item.getNumber())
                .status(item.getStatus().name())
                .internalRef(item.getInternalRef().getId())
                .rfpqId(item.getRfpq().getId())
                .rfpqNumber(item.getRfpq().getNumber())
                .currentPhase(item.getInternalRef().getCurrentPhase().name())
                .user(new UserDtoShort(item.getUser().getId(), item.getUser().getUsername()))
                .build();
    }

    public static OneBidSummaryDto mapToOneBidSummaryDto(BidSummary item) {
        Set<OneBidSummaryLineDto> lines = item.getLines().stream()
                .map(BidSummaryMapper::mapToOneBidSummaryLineDto)
                .collect(java.util.stream.Collectors.toSet());
        return OneBidSummaryDto.builder()
                .id(item.getId())
                .number(item.getNumber())
                .status(item.getStatus().name())
                .fromDate(item.getFromDate().toString())
                .updatedAt(item.getUpdatedAt().toString())
                .internalRef(item.getInternalRef().getId())
                .currentPhase(item.getInternalRef().getCurrentPhase().name())
                .rfpqId(item.getRfpq().getId())
                .rfpqNumber(item.getRfpq().getNumber())
                .user(new UserDtoShort(item.getUser().getId(), item.getUser().getUsername()))
                .lines(lines)
                .build();
    }

    public static OneBidSummaryLineDto mapToOneBidSummaryLineDto(BidSummaryLine item) {

        return OneBidSummaryLineDto.builder()
                .id(item.getId())
                .price(item.getPrice())
                .vat(item.getVat())
                .total(item.getTotal())
                .quantity(item.getQuantity())
                .quotationId(item.getQuotation().getId())
                .productId(item.getProduct().getId())
                .supplierId(item.getSupplier().getId())
                .build();
    }
}
