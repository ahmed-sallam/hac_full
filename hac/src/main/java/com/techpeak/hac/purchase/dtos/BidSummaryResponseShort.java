package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.core.dtos.UserDtoShort;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidSummaryResponseShort implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private String number;
    private String status;
    private Long internalRef;
    private String currentPhase;
    private Long rfpqId;
    private String rfpqNumber;
    private UserDtoShort user;
}