package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.BidSummaryResponseShort;
import com.techpeak.hac.purchase.dtos.bid_summary.CreateBidSummaryDto;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.BidSummary;
import com.techpeak.hac.purchase.models.BidSummaryLine;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import com.techpeak.hac.purchase.repositories.BidSummaryRepository;
import com.techpeak.hac.purchase.services.BidSummaryService;
import com.techpeak.hac.purchase.services.PurchaseOrderService;
import com.techpeak.hac.purchase.services.SupplierQuotationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidSummaryServiceImpl implements BidSummaryService {
    private final BidSummaryRepository bidSummaryRepository;
    private final RFPQServiceImpl rfpqService;
    private final ProductService productService;
    private final SupplierQuotationService supplierQuotationService;
    private final UserHistoryService userHistoryService;
    private final PurchaseOrderService purchaseOrderService;

    private static BidSummaryResponseShort mapToShortResponse(BidSummary item) {
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

    @Override
    @Transactional
    public BidSummary createBidSummary(CreateBidSummaryDto request, User user) {
        InternalRef internalRef = rfpqService.getOrElseThrow(request.rfpqId()).getInternalRef();
        if (request.status() == RequestStatus.PENDING) {
            internalRef.setCurrentPhase(InternalPhase.BID_SUMMARY);
        }
        Optional<BidSummary> lastOne = bidSummaryRepository.findTopByOrderByNumberDesc();
        String bidSummaryNumber = GenerateRequestNumber.generateRequestNumber("BS", lastOne.map(BidSummary::getNumber).orElse(null));
        BidSummary bidSummary = buildBidSummary(request, user, bidSummaryNumber, internalRef);
        Set<BidSummaryLine> lines = getBidSummaryLines(request);
        bidSummary.setLines(lines);
        BidSummary savedBidSummary = bidSummaryRepository.save(bidSummary);
        String actionDetails = "Generated a new Bid Summary (" + savedBidSummary.getStatus().name() + ") with number:" + savedBidSummary.getNumber() + " and internal id: " + savedBidSummary.getInternalRef().getId();
        userHistoryService.createUserHistory(user, savedBidSummary.getId(), actionDetails, "bid_summary");
        return savedBidSummary;
    }

    @Override
    @Transactional
    public void updateStatus(Long id, RequestStatus status, User user) {
        BidSummary bidSummary = getOrElseThrow(id);
        if (status == RequestStatus.PENDING) {
            bidSummary.getInternalRef().setCurrentPhase(InternalPhase.BID_SUMMARY);
        }
        if (status == RequestStatus.PROCESSING) {
            bidSummary.getInternalRef().setCurrentPhase(InternalPhase.PURCHASE_ORDER);
            // adding POs according to selected suppliers.
            purchaseOrderService.createPurchaseOrderInternal(bidSummary, user);
        }
        bidSummary.setStatus(status);
        bidSummaryRepository.save(bidSummary);
        String actionDetails = "Updated the status of Bid Summary with number:" + bidSummary.getNumber() + " and internal id: " + bidSummary.getInternalRef().getId() + " to " + status.name();
        userHistoryService.createUserHistory(user, bidSummary.getId(), actionDetails, "bid_summary");
    }

    @Override
    public Page<BidSummaryResponseShort> search(int page, int size, String sort, String search, Long ref, Long user, String phase, String status) {
        Specification<BidSummary> specification = Specification.where(null);
        if (ref != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("internalRef").get("id"), ref));
        }

        if (user != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), user));
        }
        if (phase != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("internalRef").get("currentPhase"), InternalPhase.valueOf(phase.toUpperCase())));
        }
        if (status != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), RequestStatus.valueOf(status.toUpperCase())));
        }
        if (search != null && !search.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("number"), "%" + search + "%"));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<BidSummary> bidSummaries = bidSummaryRepository.findAll(specification, pageable);
        return bidSummaries.map(BidSummaryServiceImpl::mapToShortResponse);
    }

    private BidSummary buildBidSummary(CreateBidSummaryDto request, User user, String bidSummaryNumber, InternalRef internalRef) {
        return BidSummary.builder()
                .number(bidSummaryNumber)
                .status(request.status())
                .fromDate(request.fromDate())
                .internalRef(internalRef)
                .rfpq(rfpqService.getOrElseThrow(request.rfpqId()))
                .user(user)
                .build();
    }

    private Set<BidSummaryLine> getBidSummaryLines(CreateBidSummaryDto request) {
        return request.lines().stream()
                .map(line -> {
                    SupplierQuotation supplierQuotation = supplierQuotationService.getSupplierQuotation(line.quotationId());
                    return BidSummaryLine.builder()
                            .product(productService.getProductOrThrow(line.productId()))
                            .quantity(line.quantity())
                            .quotation(supplierQuotation)
                            .supplier(supplierQuotation.getSupplier())
                            .price(line.price())
                            .vat(line.vat())
                            .total( line.quantity() * (line.price() + line.vat()))
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private BidSummary getOrElseThrow(Long id) {
        return bidSummaryRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Bid Summary not found"));
    }
}
