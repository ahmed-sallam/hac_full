package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.BidSummaryResponseShort;
import com.techpeak.hac.purchase.dtos.bid_summary.CreateBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.OneBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.UpdateBidSummaryLineDto;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.BidSummary;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BidSummaryService {
    BidSummary createBidSummary(CreateBidSummaryDto request, User user);

    void updateStatus(Long id, RequestStatus status, User user);

    Page<BidSummaryResponseShort> search(int page, int size, String sort, String search, Long ref, Long user, String phase, String status);

    OneBidSummaryDto getOneBidSummary(Long id);

    void updateBidSummary(Long id, List<UpdateBidSummaryLineDto> request,
                          User user);

}
