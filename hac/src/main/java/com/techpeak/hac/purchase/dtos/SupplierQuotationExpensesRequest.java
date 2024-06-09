package com.techpeak.hac.purchase.dtos;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotationExpenses}
 */
@Value
public class SupplierQuotationExpensesRequest implements Serializable {
    Long expensesTitleId;
    Double amount;
    String notes;
}