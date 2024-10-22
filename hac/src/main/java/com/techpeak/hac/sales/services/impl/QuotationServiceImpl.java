package com.techpeak.hac.sales.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.core.services.CurrencyService;
import com.techpeak.hac.core.services.InternalRefService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.sales.dtos.CreateQuotationRequest;
import com.techpeak.hac.sales.dtos.CreateQuotationRequestLine;
import com.techpeak.hac.sales.dtos.QuotationResponse;
import com.techpeak.hac.sales.dtos.QuotationResponseShort;
import com.techpeak.hac.sales.dtos.mappers.QuotationMapper;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.models.Quotation;
import com.techpeak.hac.sales.models.QuotationLine;
import com.techpeak.hac.sales.repositories.QuotationRepository;
import com.techpeak.hac.sales.services.CustomerService;
import com.techpeak.hac.sales.services.QuotationService;
import com.techpeak.hac.sales.services.SaleOrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {
    private final QuotationRepository repository;
    private final CurrencyService currencyService;
    private final InternalRefService internalRefService;
    private final ProductService productService;
    private final UserHistoryService userHistoryService;
    private final CustomerService customerService;
    private final SaleOrderService saleOrderService;

    @Override
    @Transactional
    public Long createQuotation(CreateQuotationRequest request, User user) {
        Optional<Quotation> lastOne = repository.findTopByOrderByNumberDesc();
        String number = GenerateRequestNumber.generateRequestNumber("SALQ",
                lastOne.map(Quotation::getNumber).orElse(null));

        CurrencyEntity currencyById = currencyService.getCurrencyById(request.getCurrency());
        InternalRef internalRefById = new InternalRef();
        internalRefById.setCurrentPhase(InternalPhase.SALES_QUOTATION);
        Customer customer = customerService.get(request.getCustomer());
        Quotation quotation = QuotationMapper.toQuotation(request, customer,
                currencyById, user);
        quotation.setInternalRef(internalRefById);
        Set<CreateQuotationRequestLine> lines = request.getLines();
        final BigDecimal[] subTotal = { BigDecimal.ZERO };
        quotation.setLines(lines.stream().map(l -> {
            QuotationLine line = QuotationMapper.toQuotationLine(
                    l,
                    productService.getProductOrThrow(l.getProductId()));
            subTotal[0] = subTotal[0].add(line.getLineTotal());
            line.setQuotation(quotation);
            return line;
        }

        ).collect(Collectors.toSet()));
        quotation.setSubTotal(subTotal[0]);
        quotation.setDiscount(request.getDiscount());
        BigDecimal subtotal = subTotal[0].subtract(request.getDiscount());
        BigDecimal vat = subtotal.multiply(BigDecimal.valueOf(0.15));
        quotation.setVat(vat);
        quotation.setTotal(subtotal.add(vat));
        quotation.setDate(LocalDate.now());
        quotation.setNumber(number);
        Quotation saved = repository.save(quotation);
        String actionDetails = "Created a new Quotation  related to internal id: " + saved.getInternalRef().getId();
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "quotations");
        return saved.getId();
    }

    @Override
    public QuotationResponse getOne(Long id) {
        List<Object[]> result = repository.findByIdWithLines(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Quotation not found with id " + id);
        }
        Quotation quotation = (Quotation) result.get(0)[0];
        Set<UserHistory> userHistories = new HashSet<>();
        for (Object[] r : result) {
            userHistories.add((UserHistory) r[1]);
        }
        quotation.setUserHistories(userHistories);
        return QuotationMapper.toQuotationResponse(quotation);
    }

    @Override
    public Page<QuotationResponseShort> search(int page, int size,
            String sort, Long ref,
            Long customer, Long user, String date, String quotation) {
        Specification<Quotation> spec = Specification.where(null);
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
        if (quotation != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("number"), "%" + quotation + "%"));
        }
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<Quotation> all = repository.findAll(spec, pageRequest);
        return all.map(QuotationMapper::toQuotationResponseShort);
    }

    @Override
    public Page<QuotationResponseShort> getAllActiveQuotations() {
        return null;
    }

    @Override
    public void updateStatus(Long id, RequestStatus status, User user) {

        Quotation quotation = getOrElseThrow(id);
        quotation.setStatus(status);
        repository.save(quotation);

        // create sale order draft
        if (status.name().equals(RequestStatus.APPROVED.name())) {
            saleOrderService.createInternal(quotation, user);
        }
        Quotation saved = repository.save(quotation);
        String actionDetails = "Update Quotation with number: " + saved.getNumber() + " and internal id: "
                + saved.getInternalRef().getId() + " status >> (" + saved.getStatus().name() + ") ";
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "quotations");
    }

    private Quotation getOrElseThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Quotation not found with id " + id));
    }
}
