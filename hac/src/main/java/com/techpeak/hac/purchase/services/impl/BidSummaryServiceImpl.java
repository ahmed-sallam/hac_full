package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.BidSummaryResponseShort;
import com.techpeak.hac.purchase.dtos.bid_summary.CreateBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.OneBidSummaryDto;
import com.techpeak.hac.purchase.dtos.bid_summary.UpdateBidSummaryLineDto;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.mappers.BidSummaryMapper;
import com.techpeak.hac.purchase.models.BidSummary;
import com.techpeak.hac.purchase.models.BidSummaryLine;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import com.techpeak.hac.purchase.repositories.BidSummaryLineRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private final BidSummaryLineRepository bidSummaryLineRepository;


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
        Set<BidSummaryLine> lines = createBidSummaryLines(request);
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
        return bidSummaries.map(BidSummaryMapper::mapToShortResponse);
    }

    @Override
    public OneBidSummaryDto getOneBidSummary(Long id) {
        BidSummary bidSummary = getOrElseThrow(id);
//        Hibernate.initialize(bidSummary.getLines());
        OneBidSummaryDto oneBidSummaryDto = BidSummaryMapper.mapToOneBidSummaryDto(bidSummary);
        oneBidSummaryDto.setGenerateBidSummary(supplierQuotationService.getSupplierQuotationsGrouped(bidSummary.getRfpq().getId(), bidSummary.getFromDate(), LocalDate.from(bidSummary.getUpdatedAt())));
        return oneBidSummaryDto;
    }

    @Override
    @Transactional
    public void updateBidSummary(Long id, List<UpdateBidSummaryLineDto> request, User user) {
        //TODO: improve user history .
        BidSummary bidSummary = getOrElseThrow(id);
        List<UpdateBidSummaryLineDto> updateItemsDto = new ArrayList<>();
        List<UpdateBidSummaryLineDto> newItems = new ArrayList<>();
        for (UpdateBidSummaryLineDto item : request) {
            if (item.id() != null) {
                updateItemsDto.add(item);
            } else {
                newItems.add(item);
            }
        }
        List<BidSummaryLine> previousItems = new ArrayList<>(bidSummary.getLines());
        List<BidSummaryLine> deletedItems = previousItems.stream()
                .filter(previousItem -> updateItemsDto.stream()
                        .noneMatch(updateItem -> updateItem.id().equals(previousItem.getId())))
                .toList();

        bidSummary.getLines().forEach(bidSummaryLine -> {
            Optional<UpdateBidSummaryLineDto> updateItem = updateItemsDto.stream()
                    .filter(item -> item.id().equals(bidSummaryLine.getId()))
                    .findFirst();
            updateItem.ifPresent(item -> {
                bidSummaryLine.setPrice(item.price());
                bidSummaryLine.setQuantity(item.quantity());
                bidSummaryLine.setVat(item.vat());
                bidSummaryLine.setTotal(item.quantity() * (item.price() + item.vat()));
                bidSummaryLine.setProduct(productService.getProductOrThrow(item.productId()));
                bidSummaryLine.setQuotation(supplierQuotationService.getSupplierQuotation(item.quotationId()));
                bidSummaryLine.setSupplier(bidSummaryLine.getQuotation().getSupplier());
            });
        });

        bidSummary.getLines().addAll(updateBidSummaryLines(newItems, bidSummary));

        bidSummary.getLines().removeAll(deletedItems);
        bidSummaryLineRepository.deleteAll(deletedItems);

        setUserHistoryForUpdatedBidsummary(user, bidSummary, newItems, updateItemsDto, deletedItems);
        bidSummaryRepository.save(bidSummary);
    }

    private void setUserHistoryForUpdatedBidsummary(User user, BidSummary bidSummary, List<UpdateBidSummaryLineDto> newItems, List<UpdateBidSummaryLineDto> updateItemsDto, List<BidSummaryLine> deletedItems) {
        String actionDetails = "Updated the Bid Summary with number:" + bidSummary.getNumber() + " and internal id: " + bidSummary.getInternalRef().getId();
        if (newItems.size() > 0) {
            actionDetails += " with new items: ";
            for (UpdateBidSummaryLineDto item : newItems) {
                actionDetails += "Product: " + productService.getProductOrThrow(item.productId()).getProductNumber() + " Quantity: " + item.quantity() + " Price: " + item.price() + " VAT: " + item.vat() + " Total: " + item.total() + " \n";
            }
        }
        if (updateItemsDto.size() > 0) {
            actionDetails += " with updated items: ";
            for (UpdateBidSummaryLineDto item : updateItemsDto) {
                actionDetails += "Product: " + productService.getProductOrThrow(item.productId()).getProductNumber() + " Quantity: " + item.quantity() + " Price: " + item.price() + " VAT: " + item.vat() + " Total: " + item.total() + " \n";
            }
        }
        if (deletedItems.size() > 0) {
            actionDetails += " with deleted items: ";
            for (BidSummaryLine item : deletedItems) {
                actionDetails += "Product: " + item.getProduct().getProductNumber() + " Quantity: " + item.getQuantity() + " Price: " + item.getPrice() + " VAT: " + item.getVat() + " Total: " + item.getTotal() + " \n";
            }
        }

        userHistoryService.createUserHistory(user, bidSummary.getId(), actionDetails, "bid_summary");
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

    private Set<BidSummaryLine> createBidSummaryLines(CreateBidSummaryDto request) {
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
                            .total(line.quantity() * (line.price() + line.vat()))
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private Set<BidSummaryLine> updateBidSummaryLines(List<UpdateBidSummaryLineDto> lines, BidSummary bidSummary) {
        return lines.stream()
                .map(line -> {
                    SupplierQuotation supplierQuotation = supplierQuotationService.getSupplierQuotation(line.quotationId());
                    BidSummaryLine bidSummaryLine = BidSummaryLine.builder()
                            .product(productService.getProductOrThrow(line.productId()))
                            .quantity(line.quantity())
                            .quotation(supplierQuotation)
                            .supplier(supplierQuotation.getSupplier())
                            .price(line.price())
                            .vat(line.vat())
                            .total(line.quantity() * (line.price() + line.vat()))
                            .build();
                    bidSummaryLine.setBidSummary(bidSummary);
                    return bidSummaryLine;
                })
                .collect(Collectors.toSet());
    }

    private BidSummary getOrElseThrow(Long id) {
        return bidSummaryRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Bid Summary not found"));
    }
}
