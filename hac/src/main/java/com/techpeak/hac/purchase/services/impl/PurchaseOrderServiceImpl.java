package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.purchase_order.CreatePurchaseOrder;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderDto;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.mappers.PurchaseOrderMapper;
import com.techpeak.hac.purchase.models.*;
import com.techpeak.hac.purchase.repositories.PurchaseOrderRepository;
import com.techpeak.hac.purchase.services.PurchaseOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final UserHistoryService userHistoryService;

    @Override
    public PurchaseOrder createPurchaseOrder(CreatePurchaseOrder purchaseOrderDto) {
        return null;
    }

    @Override
    @Transactional
    public void createPurchaseOrderInternal(BidSummary bidSummary, User user) {
        InternalRef internalRef = bidSummary.getInternalRef();
        internalRef.setCurrentPhase(InternalPhase.PURCHASE_ORDER);
        Map<Supplier, Set<BidSummaryLine>> supplierLines = bidSummary.getLines().stream()
                .collect(Collectors.groupingBy(BidSummaryLine::getSupplier, Collectors.toSet()));
        supplierLines.forEach((supplier, lines) -> {
            Optional<PurchaseOrder> lastOne = purchaseOrderRepository.findTopByOrderByNumberDesc();
            String purchaseOrderNumber = GenerateRequestNumber.generateRequestNumber("PO", lastOne.map(PurchaseOrder::getNumber).orElse(null));

            Double subTotal = lines.stream().mapToDouble(
                    (item) -> item.getPrice() * item.getQuantity()
            ).sum();
            Double vat = lines.stream().mapToDouble(
                    (item) -> item.getVat() * item.getQuantity()
            ).sum();
            Double total = subTotal + vat;

            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setNumber(purchaseOrderNumber);
            purchaseOrder.setDate(LocalDate.now());
            purchaseOrder.setStatus(RequestStatus.PROCESSING);
            purchaseOrder.setStore(bidSummary.getRfpq().getStore());
            purchaseOrder.setInternalRef(internalRef);
            purchaseOrder.setUser(user);
            purchaseOrder.setSupplier(supplier);
            purchaseOrder.setSubTotal(subTotal);
            purchaseOrder.setVat(vat);
            purchaseOrder.setTotal(total);
            Set<PurchaseOrderLine> purchaseOrderLines = lines.stream()
                    .map(line -> {
                        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
                        purchaseOrderLine.setProduct(line.getProduct());
                        purchaseOrderLine.setQuantity(line.getQuantity());
                        purchaseOrderLine.setPrice(line.getPrice());
                        purchaseOrderLine.setVat(line.getVat());
                        purchaseOrderLine.setTotal(line.getTotal());
                        return purchaseOrderLine;
                    })
                    .collect(Collectors.toSet());
            purchaseOrder.setLines(purchaseOrderLines);
            PurchaseOrder saved = purchaseOrderRepository.saveAndFlush(purchaseOrder);
            String actionDetails = "Generated a new Purchase Order (" + saved.getStatus().name() + ") with number:" + saved.getNumber() + " and internal id: " + saved.getInternalRef().getId();
            userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "purchase_orders");
        });

    }

    @Override
    public PurchaseOrderDto getPurchaseOrder(Long id) {
        PurchaseOrder purchaseOrder = getOrThrow(id);
        return PurchaseOrderMapper.toDto(purchaseOrder);
    }

    @Override
    public void updatePurchaseOrder(Long id, CreatePurchaseOrder purchaseOrderDto) {

    }

    @Override
    public Page<PurchaseOrderShort> getPurchaseOrders(int page, int size, String sort, String search, Long ref, Long store, Long user, String phase, String status, Long supplier) {
        Specification<PurchaseOrder> spec = Specification.where(null);
        if (ref != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("internalRef").get("id"), ref));
        }
        if (store != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("store").get("id"), store));
        }
        if (user != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), user));
        }
        if (phase != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("internalRef").get("currentPhase"), InternalPhase.valueOf(phase)));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), RequestStatus.valueOf(status)));
        }
        if (supplier != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("supplier").get("id"), supplier));
        }
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("number"), "%" + search + "%"));
        }
        return purchaseOrderRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sort)))
                .map(PurchaseOrderMapper::toShort);
    }

    @Override
    public PurchaseOrder getOrThrow(Long id) {
        return purchaseOrderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase Order not found"));
    }

    @Override
    public void updatePurchaseOrderStatus(Long id, RequestStatus status, User user) {
        PurchaseOrder purchaseOrder = getOrThrow(id);
        purchaseOrder.setStatus(status);
        purchaseOrderRepository.save(purchaseOrder);
        String actionDetails = "Update Purchase Order with number: " + purchaseOrder.getNumber() + " and internal id: " + purchaseOrder.getInternalRef().getId() + " status >> (" + purchaseOrder.getStatus().name() + ") ";
        userHistoryService.createUserHistory(user, purchaseOrder.getId(), actionDetails, "purchase_orders");
    }
}
