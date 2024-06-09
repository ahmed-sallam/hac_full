package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.CurrencyService;
import com.techpeak.hac.core.services.InternalRefService;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.purchase.dtos.SupplierQuotationExpensesRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationLineRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponseShort;
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
import org.springframework.stereotype.Service;

import java.util.Set;

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
    public Page<SupplierQuotationResponseShort> getAllSupplierQuotations() {
//        return supplierQuotationRepository.findAll();
        return null;
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
                .collect(java.util.stream.Collectors.toSet()));
        Set<SupplierQuotationExpensesRequest> expenses = request.getExpenses();
        if(!expenses.isEmpty())  supplierQuotation.setExpenses(expenses
                .stream()
                .map(e ->
                        SupplierQuotationExpensesMapper
                                .mapToSupplierQuotationExpenses(e,
                                        purchaseExpensesTitleService.get(e.getExpensesTitleId())))
                .collect(java.util.stream.Collectors.toSet()));
        // add to user history
        SupplierQuotation saved = supplierQuotationRepository.save(supplierQuotation);
        String actionDetails = "Created a new Supplier Quotation  related to internal id: " + saved.getInternalRef().getId();
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "supplier_quotations");
        return saved;

    }


}
