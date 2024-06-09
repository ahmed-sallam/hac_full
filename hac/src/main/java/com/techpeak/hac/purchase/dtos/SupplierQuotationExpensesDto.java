package com.techpeak.hac.purchase.dtos;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotationExpenses}
 */
@Value
@Builder
public class SupplierQuotationExpensesDto implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    PurchaseExpensesTitleResponse expensesTitle;
    Double amount;
    String notes;
}