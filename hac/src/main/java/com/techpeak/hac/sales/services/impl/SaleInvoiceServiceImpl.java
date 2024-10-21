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

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.core.services.CurrencyService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.sales.dtos.CreateSaleInvoiceRequest;
import com.techpeak.hac.sales.dtos.SaleInvoiceResponse;
import com.techpeak.hac.sales.dtos.SaleInvoiceResponseShort;
import com.techpeak.hac.sales.dtos.mappers.SaleInvoiceMapper;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.models.SaleInvoice;
import com.techpeak.hac.sales.models.SaleInvoiceLine;
import com.techpeak.hac.sales.repositories.SaleInvoiceRepository;
import com.techpeak.hac.sales.services.CustomerService;
import com.techpeak.hac.sales.services.SaleInvoiceService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleInvoiceServiceImpl implements SaleInvoiceService {
    private final SaleInvoiceRepository repository;
    private final CurrencyService currencyService;
    private final ProductService productService;
    private final UserHistoryService userHistoryService;
    private final CustomerService customerService;

    @Override
    @Transactional
    public Long createSaleInvoice(CreateSaleInvoiceRequest request, User user) {
        Optional<SaleInvoice> lastOne = repository.findTopByOrderByNumberDesc();
        String number = GenerateRequestNumber.generateRequestNumber("SALINV",
                lastOne.map(SaleInvoice::getNumber).orElse(null));

        CurrencyEntity currencyById = currencyService.getCurrencyById(request.getCurrency());
        Customer customer = customerService.get(request.getCustomer());
        SaleInvoice saleInvoice = SaleInvoiceMapper.toSaleInvoice(request, customer, currencyById, user);
        Set<SaleInvoiceLine> lines = request.getLines().stream()
                .map(l -> SaleInvoiceMapper.toSaleInvoiceLine(l, productService.getProductOrThrow(l.getProductId())))
                .collect(Collectors.toSet());
        saleInvoice.setLines(lines);

        BigDecimal subTotal = lines.stream().map(SaleInvoiceLine::getLineTotal).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        saleInvoice.setSubTotal(subTotal);
        saleInvoice.setDiscount(request.getDiscount());
        BigDecimal total = subTotal.subtract(request.getDiscount()).add(saleInvoice.getVat());
        saleInvoice.setTotal(total);
        saleInvoice.setDate(LocalDate.now());
        saleInvoice.setNumber(number);

        SaleInvoice saved = repository.save(saleInvoice);
        String actionDetails = "Created a new Sale Invoice related to customer id: " + saved.getCustomer().getId();
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "sale_invoices");
        return saved.getId();
    }

    @Override
    public SaleInvoiceResponse getOne(Long id) {
        List<Object[]> result = repository.findByIdWithLines(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Sale Invoice not found with id " + id);
        }
        SaleInvoice saleInvoice = (SaleInvoice) result.get(0)[0];
        Set<UserHistory> userHistories = new HashSet<>();
        for (Object[] r : result) {
            userHistories.add((UserHistory) r[1]);
        }
        saleInvoice.setUserHistories(userHistories);
        return SaleInvoiceMapper.toSaleInvoiceResponse(saleInvoice);
    }

    @Override
    public Page<SaleInvoiceResponseShort> search(int page, int size, String sort, Long ref, Long customer, Long user,
            String date, String invoice) {
        Specification<SaleInvoice> spec = Specification.where(null);
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
        if (invoice != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("number"), "%" + invoice + "%"));
        }
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<SaleInvoice> all = repository.findAll(spec, pageRequest);
        return all.map(SaleInvoiceMapper::toSaleInvoiceResponseShort);
    }

    @Override
    public Page<SaleInvoiceResponseShort> getAllActiveSaleInvoices() {
        // Implement logic to fetch all active sale invoices
        return null;
    }
}