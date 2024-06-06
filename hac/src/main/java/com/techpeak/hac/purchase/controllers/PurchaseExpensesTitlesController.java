package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.purchase.dtos.PurchaseExpensesTitleResponse;
import com.techpeak.hac.purchase.services.PurchaseExpensesTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PurchaseExpensesTitlesController {
    private final PurchaseExpensesTitleService purchaseExpensesTitleService;

    @GetMapping("/api/v1/purchase-expenses/titles")
    public ResponseEntity<List<PurchaseExpensesTitleResponse>> list(){
        List<PurchaseExpensesTitleResponse> all =  purchaseExpensesTitleService.list();
        return ResponseEntity.ok(all);
    }
}
