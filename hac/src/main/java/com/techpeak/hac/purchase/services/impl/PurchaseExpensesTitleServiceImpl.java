package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.purchase.dtos.PurchaseExpensesTitleResponse;
import com.techpeak.hac.purchase.models.PurchaseExpensesTitle;
import com.techpeak.hac.purchase.repositories.PurchaseExpensesTitleRepository;
import com.techpeak.hac.purchase.services.PurchaseExpensesTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PurchaseExpensesTitleServiceImpl implements PurchaseExpensesTitleService {
    private final PurchaseExpensesTitleRepository purchaseExpensesTitleRepository;
    @Override
    public List<PurchaseExpensesTitleResponse> list() {
        List<PurchaseExpensesTitle> all = purchaseExpensesTitleRepository.findAll();
        return all.stream().map(this::toResponse).toList();
    }
    private PurchaseExpensesTitleResponse toResponse(PurchaseExpensesTitle purchaseExpensesTitle){
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
