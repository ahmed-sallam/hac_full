package com.techpeak.hac.purchase.dtos.bid_summary;

import com.techpeak.hac.core.dtos.UserDtoShort;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneBidSummaryDto implements Serializable {
    private Long id;
    private String number;
    private String status;
    private String fromDate;
    private String updatedAt;
    private Long internalRef;
    private String currentPhase;
    private Long rfpqId;
    private String rfpqNumber;
    private UserDtoShort user;

    private Set<OneBidSummaryLineDto> lines;
    private GenerateBidSummaryDto generateBidSummary;
}
