package com.techpeak.hac.purchase.services;

import com.techpeak.hac.purchase.dtos.PurchaseExpensesTitleResponse;
import com.techpeak.hac.purchase.models.PurchaseExpensesTitle;

import java.util.List;


public interface PurchaseExpensesTitleService {
    List<PurchaseExpensesTitleResponse> list();

    PurchaseExpensesTitle get(Long id);
}
