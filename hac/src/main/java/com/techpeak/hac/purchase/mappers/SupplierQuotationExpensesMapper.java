package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.purchase.dtos.SupplierQuotationExpensesRequest;
import com.techpeak.hac.purchase.models.PurchaseExpensesTitle;
import com.techpeak.hac.purchase.models.SupplierQuotationExpenses;

public class SupplierQuotationExpensesMapper {
    private SupplierQuotationExpensesMapper() {
    }

    public static SupplierQuotationExpenses mapToSupplierQuotationExpenses(SupplierQuotationExpensesRequest request, PurchaseExpensesTitle expensesTitle) {
    return SupplierQuotationExpenses.builder()
            .amount(request.getAmount())
            .expensesTitle(expensesTitle)
            .notes(request.getNotes())
            .build();
    }


}
