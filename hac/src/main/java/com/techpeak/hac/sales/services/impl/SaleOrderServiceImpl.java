package com.techpeak.hac.sales.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.core.services.CurrencyService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.dtos.InventoryTransactionLineRequest;
import com.techpeak.hac.inventory.dtos.InventoryTransactionRequest;
import com.techpeak.hac.inventory.enums.TransactionType;
import com.techpeak.hac.inventory.services.InventoryTransactionService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.StoreService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.mappers.UserHistoryMapper;
import com.techpeak.hac.sales.dtos.CreateSaleOrder;
import com.techpeak.hac.sales.dtos.SaleOrderResponse;
import com.techpeak.hac.sales.dtos.SaleOrderResponseShort;
import com.techpeak.hac.sales.dtos.mappers.SaleOrderMapper;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.models.Quotation;
import com.techpeak.hac.sales.models.SaleOrder;
import com.techpeak.hac.sales.models.SaleOrderLine;
import com.techpeak.hac.sales.repositories.SaleOrderRepository;
import com.techpeak.hac.sales.services.CustomerService;
import com.techpeak.hac.sales.services.SaleOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {
    private final SaleOrderRepository repository;
    private final CurrencyService currencyService;
    private final ProductService productService;
    private final UserHistoryService userHistoryService;
    private final CustomerService customerService;
    private final StoreService storeService;
    private final InventoryTransactionService inventoryTransactionService;

    @Override
    @Transactional
    public Long createSaleOrder(CreateSaleOrder request, User user) {
        Optional<SaleOrder> lastOne = repository.findTopByOrderByNumberDesc();
        String number = GenerateRequestNumber.generateRequestNumber("SO",
                lastOne.map(SaleOrder::getNumber).orElse(null));

        CurrencyEntity currencyById = currencyService.getCurrencyById(request.getCurrency());
        Customer customer = customerService.get(request.getCustomer());
        SaleOrder saleOrder = SaleOrderMapper.toSaleOrder(request, customer, currencyById, user);
        Set<SaleOrderLine> lines = request.getLines().stream()
                .map(l -> SaleOrderMapper.toSaleOrderLine(l, productService.getProductOrThrow(l.getProductId())))
                .collect(Collectors.toSet());
        saleOrder.setLines(lines);

        BigDecimal subTotal = lines.stream().map(SaleOrderLine::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        saleOrder.setSubTotal(subTotal);
        saleOrder.setDiscount(request.getDiscount());
        BigDecimal total = subTotal.subtract(request.getDiscount()).add(saleOrder.getVat());
        saleOrder.setTotal(total);
        saleOrder.setDate(LocalDate.now());
        saleOrder.setNumber(number);
        saleOrder.setStore(storeService.getOrElseThrow(request.getStore()));
        SaleOrder saved = repository.save(saleOrder);
        String actionDetails = "Created a new Sale Order related to customer id: " + saved.getCustomer().getId();
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "sale_orders");
        return saved.getId();
    }

    @Override
    public SaleOrderResponse getOne(Long id) {
        List<Object[]> result = repository.findByIdWithLines(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Sale Order not found with id " + id);
        }
        SaleOrder saleOrder = (SaleOrder) result.get(0)[0];
        Set<UserHistory> userHistories = new HashSet<>();
        for (Object[] r : result) {
            userHistories.add((UserHistory) r[1]);
        }
        SaleOrderResponse res = SaleOrderMapper.toSaleOrderResponse(saleOrder);
        res.setUserHistories(
                (userHistories.stream().map(UserHistoryMapper::mapToDto
                ).collect(Collectors.toSet())));
        return res;
    }

    @Override
    public Page<SaleOrderResponseShort> search(int page, int size, String sort, Long ref, Long customer, Long user,
                                               String date, String order) {
        Specification<SaleOrder> spec = Specification.where(null);
        if (ref != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("internalRef").get("id"), ref));
        }
        if (customer != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("customer").get("id"), customer));
        }
        if (user != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), user));
        }
        if (date != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("date"), date));
        }
        if (order != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("number"), "%" + order + "%"));
        }
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<SaleOrder> all = repository.findAll(spec, pageRequest);
        return all.map(SaleOrderMapper::toSaleOrderResponseShort);
    }

    @Override
    public Page<SaleOrderResponseShort> getAllActiveSaleOrders() {
        // Implement logic to fetch all active sale orders
        return null;
    }

    @Override
    @Transactional
    public void createInternal(Quotation quotation, User user) {
        // Generate a new sale order number
        Optional<SaleOrder> lastOne = repository.findTopByOrderByNumberDesc();
        String number = GenerateRequestNumber.generateRequestNumber("SO",
                lastOne.map(SaleOrder::getNumber).orElse(null));

        // Create a new SaleOrder object
        SaleOrder saleOrder = SaleOrder.builder()
                .number(number)
                .status(RequestStatus.DRAFT)
                .customer(quotation.getCustomer())
                .currency(quotation.getCurrency())
                .user(user)
                .date(LocalDate.now())
                .subTotal(quotation.getSubTotal())
                .discount(quotation.getDiscount())
                .vat(quotation.getVat())
                .total(quotation.getTotal())
                .internalRef(quotation.getInternalRef())
                .paymentTerms(quotation.getPaymentTerms())
                .build();

        // Map QuotationLines to SaleOrderLines
        Set<SaleOrderLine> saleOrderLines = quotation.getLines().stream()
                .map(quotationLine -> SaleOrderLine.builder()
                        .product(quotationLine.getProduct())
                        .quantity(quotationLine.getQuantity())
                        .price(quotationLine.getPrice())
                        .discount(quotationLine.getDiscount())
                        .lineTotal(quotationLine.getLineTotal())
                        .build())
                .collect(Collectors.toSet());

        saleOrder.setLines(saleOrderLines);

        // Save the SaleOrder
        SaleOrder savedSaleOrder = repository.save(saleOrder);

        // Create user history
        String actionDetails = "Created a new Sale Order from Quotation with number: " + savedSaleOrder.getNumber();
        userHistoryService.createUserHistory(user, savedSaleOrder.getId(), actionDetails, "sale_orders");

    }

    @Override
    @Transactional
    public void updateStatus(Long id, RequestStatus status, User user) {
        SaleOrder saleOrder = getOrElseThrow(id);
        if (status.name().equals(RequestStatus.PROCESSING.name())) {
            InventoryTransactionRequest request =
                    new InventoryTransactionRequest(saleOrder.getDate().atStartOfDay(),
                            saleOrder.getStore().getId(),
                            storeService.getOrElseThrow(1L).getId(),
                            TransactionType.SALE.name(),
                            RequestStatus.PENDING.name(),
                            saleOrder.getInternalRef().getId(),
                            saleOrder.getLines().stream().map(l -> new InventoryTransactionLineRequest(l.getProduct().getId(), l.getQuantity(), l.getPrice())).collect(Collectors.toSet()));
            inventoryTransactionService.createInventoryTransaction(request, user);
        } else if (status.name().equals(RequestStatus.CANCELED.name())) {
            // get all inventory transaction related to this sale order and set
            // status to canceled by related by internal ref id
            inventoryTransactionService.cancelInventoryTransaction(saleOrder.getInternalRef().getId(), user);
        }
        saleOrder.setStatus(RequestStatus.PROCESSING);
        SaleOrder saved = repository.save(saleOrder);
        String actionDetails = "Update Sale Order with number: " + saved.getNumber() + " and internal id: "
                + saved.getInternalRef().getId() + " status >> (" + saved.getStatus().name() + ") ";
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "sale_orders");
    }

    private SaleOrder getOrElseThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Sale " +
                "Order with id " + id + " not found"));
    }
}
