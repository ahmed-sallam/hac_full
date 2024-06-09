package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.purchase.dtos.PurchaseExpensesTitleResponse;
import com.techpeak.hac.purchase.dtos.SupplierQuotationExpensesDto;
import com.techpeak.hac.purchase.models.PurchaseExpensesTitle;
import com.techpeak.hac.purchase.models.SupplierQuotationExpenses;

public class PurchaseExpensesMapper {
    private PurchaseExpensesMapper() {
    }

    public static SupplierQuotationExpensesDto mapToDto (SupplierQuotationExpenses supplierQuotationExpenses) {
        return SupplierQuotationExpensesDto.builder()
                .id(supplierQuotationExpenses.getId())
                .amount(supplierQuotationExpenses.getAmount())
                .notes(supplierQuotationExpenses.getNotes())
                .expensesTitle(PurchaseExpensesMapper.mapToTitleDto(supplierQuotationExpenses.getExpensesTitle()))
                .build();
    }

    public static PurchaseExpensesTitleResponse mapToTitleDto(PurchaseExpensesTitle purchaseExpensesTitle) {
        return PurchaseExpensesTitleResponse.builder()
                .id(purchaseExpensesTitle.getId())
                .nameAr(purchaseExpensesTitle.getNameAr())
                .nameEn(purchaseExpensesTitle.getNameEn())
                .isActive(purchaseExpensesTitle.getIsActive())
                .createdAt(purchaseExpensesTitle.getCreatedAt())
                .updatedAt(purchaseExpensesTitle.getUpdatedAt())
                .build();
    }
}
