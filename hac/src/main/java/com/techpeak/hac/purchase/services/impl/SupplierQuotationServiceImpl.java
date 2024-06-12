package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.core.services.CurrencyService;
import com.techpeak.hac.core.services.InternalRefService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.purchase.dtos.*;
import com.techpeak.hac.purchase.mappers.SupplierQuotationExpensesMapper;
import com.techpeak.hac.purchase.mappers.SupplierQuotationLineMapper;
import com.techpeak.hac.purchase.mappers.SupplierQuotationMapper;
import com.techpeak.hac.purchase.models.RFPQ;
import com.techpeak.hac.purchase.models.Supplier;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import com.techpeak.hac.purchase.repositories.SupplierQuotationRepository;
import com.techpeak.hac.purchase.services.PurchaseExpensesTitleService;
import com.techpeak.hac.purchase.services.SupplierQuotationService;
import com.techpeak.hac.purchase.services.SupplierService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierQuotationServiceImpl implements SupplierQuotationService {
    private final SupplierQuotationRepository supplierQuotationRepository;
    private final CurrencyService currencyService;
    private final InternalRefService internalRefService;
    private final RFPQServiceImpl rfpqService;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final PurchaseExpensesTitleService purchaseExpensesTitleService;
    private final UserHistoryService userHistoryService;

    @Override
    public Page<SupplierQuotationResponseShort> search(int page, int size, String sort, Long ref, Long supplier, Long user, String supplierRef, Boolean isLocal, String date, String rfpq) {
        Specification<SupplierQuotation> spec = Specification.where(null);
        if (ref != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("internalRef").get("id"), ref));
        }
        if (supplier != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("supplier").get("id"), supplier));
        }
        if (user != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), user));
        }
        if (supplierRef != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("supplierRef"), supplierRef));
        }
        if (isLocal != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isLocal"), isLocal));
        }
        if (date != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("date"), date));
        }
        if(rfpq != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("rfpq").get("number"), "%" + rfpq + "%" ));
        }

        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<SupplierQuotation> all = supplierQuotationRepository.findAll( spec, pageRequest);
        return all.map(SupplierQuotationMapper::mapToResponseShort);
    }

    @Override
    public Page<SupplierQuotationResponseShort> getAllActiveSupplierQuotations() {
//        return supplierQuotationRepository.findAllByIsActive(true);

        return null;
    }

    @Override
    @Transactional
    public SupplierQuotation createSupplierQuotation(SupplierQuotationRequest request, User user) {
        CurrencyEntity currencyById = currencyService.getCurrencyById(request.getCurrencyId());
        RFPQ rfpq = rfpqService.getOrElseThrow(request.getRfpqId());
        InternalRef internalRefById = rfpq.getInternalRef();
        Supplier supplier = supplierService.get(request.getSupplierId());
        SupplierQuotation supplierQuotation = SupplierQuotationMapper.mapToSupplierQuotation(request, user, currencyById, internalRefById, rfpq, supplier);
        Set<SupplierQuotationLineRequest> lines = request.getLines();
        supplierQuotation.setLines(lines
                .stream()
                .map(l ->
                        SupplierQuotationLineMapper
                                .mapToSupplierQuotationLine(l,
                                        productService
                                                .getProductOrThrow(l.getProductId())))
                .collect(Collectors.toSet()));
        Set<SupplierQuotationExpensesRequest> expenses = request.getExpenses();
        if(!expenses.isEmpty())  supplierQuotation.setExpenses(expenses
                .stream()
                .map(e ->
                        SupplierQuotationExpensesMapper
                                .mapToSupplierQuotationExpenses(e,
                                        purchaseExpensesTitleService.get(e.getExpensesTitleId())))
                .collect(Collectors.toSet()));
        // add to user history
        SupplierQuotation saved = supplierQuotationRepository.save(supplierQuotation);
        String actionDetails = "Created a new Supplier Quotation  related to internal id: " + saved.getInternalRef().getId();
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "supplier_quotations");
        return saved;

    }

    @Override
    public SupplierQuotationResponse getOne(Long id) {
        List<Object[]> result = supplierQuotationRepository.findByIdWithLines(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Supplier quotation not found with id " + id);
        }
        SupplierQuotation supplierQuotation = (SupplierQuotation) result.get(0)[0];
        Set<UserHistory> userHistories = new HashSet<>();
        for (Object[] r : result) {
            userHistories.add((UserHistory) r[1]);
        }
        supplierQuotation.setUserHistories(userHistories);
        return SupplierQuotationMapper.mapToResponse(supplierQuotation);
    }

    private SupplierQuotation getSupplierQuotation(Long id) {
        return supplierQuotationRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier quotation not found with id " + id));
    }


}
