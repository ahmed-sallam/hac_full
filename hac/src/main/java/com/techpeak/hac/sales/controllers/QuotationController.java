package com.techpeak.hac.sales.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.sales.dtos.CreateQuotationRequest;
import com.techpeak.hac.sales.dtos.QuotationResponse;
import com.techpeak.hac.sales.dtos.QuotationResponseShort;
import com.techpeak.hac.sales.services.QuotationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quotations")
@RequiredArgsConstructor
@Tag(name = "Sales Quotations", description = "Sales Quotations API")
@Validated
public class QuotationController {
    private final QuotationService quotationService;

    @GetMapping("/{id}")
    public ResponseEntity<QuotationResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateQuotationRequest request) {
        User user = AuthUtils.getCurrentUser();
        Long quotation = quotationService.createQuotation(request, user);
        return ResponseEntity.ok(quotation);
    }

    @GetMapping
    public ResponseEntity<Page<QuotationResponseShort>> search(@RequestParam(name = "page", defaultValue = "0") int page,
                                                               @RequestParam(name = "size", defaultValue = "80") int size,
                                                               @RequestParam(name = "sort", defaultValue = "id") String sort,
                                                               @RequestParam(name = "ref", required = false) Long ref,
                                                               @RequestParam(name = "customer", required = false) Long customer,
                                                               @RequestParam(name = "user", required = false) Long user,
                                                               @RequestParam(name = "date", required = false) String date,
                                                               @RequestParam(name = "quotation", required = false) String quotation) {
        return ResponseEntity.ok(quotationService.search(page, size, sort, ref, customer, user, date, quotation));
    }

}
