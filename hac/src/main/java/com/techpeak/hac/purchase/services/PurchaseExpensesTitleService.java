package com.techpeak.hac.purchase.services;

import com.techpeak.hac.purchase.dtos.PurchaseExpensesTitleResponse;

import java.util.List;


public interface PurchaseExpensesTitleService {
    List<PurchaseExpensesTitleResponse> list();
}
