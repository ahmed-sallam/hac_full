package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.purchase.dtos.BidSummaryResponseShort;
import com.techpeak.hac.purchase.dtos.bid_summary.CreateBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.OneBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.UpdateBidSummaryLineDto;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.BidSummary;
import com.techpeak.hac.purchase.services.BidSummaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/bid_summaries")
@RequiredArgsConstructor
@Validated
@Tag(name = "Bid Summary")
public class BidSummaryController {
    private final BidSummaryService bidSummaryService;

    @PostMapping
    public ResponseEntity<Long> create(
            @Valid @RequestBody CreateBidSummaryDto createBidSummaryDto) {

        User user = AuthUtils.getCurrentUser();
        BidSummary bidSummary = bidSummaryService.createBidSummary(createBidSummaryDto, user);
        return new ResponseEntity<>(bidSummary.getId(), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Resource> updateStatus(@RequestBody RequestStatus status,
                                                 @PathVariable("id") Long id) {

        User user = AuthUtils.getCurrentUser();
        bidSummaryService.updateStatus(id, status, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<BidSummaryResponseShort>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "ref", required = false) Long ref,
            @RequestParam(value = "user", required = false) Long user,
            @RequestParam(value = "phase", required = false) String phase,
            @RequestParam(value = "status", required = false) String status) {
        return ResponseEntity.ok(bidSummaryService.search(page, size, sort, search, ref, user, phase, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OneBidSummaryDto> getOneBidSummary(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bidSummaryService.getOneBidSummary(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateBidSummary(@PathVariable("id") Long id,
                                                     @RequestBody List<UpdateBidSummaryLineDto> request) {
        User user = AuthUtils.getCurrentUser();
        bidSummaryService.updateBidSummary(id, request, user);
        return ResponseEntity.ok().build();
    }
}
