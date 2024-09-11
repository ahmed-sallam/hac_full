package com.techpeak.hac.sales.services.impl;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.sales.dtos.CreateQuotationRequest;
import com.techpeak.hac.sales.dtos.QuotationResponse;
import com.techpeak.hac.sales.services.QuotationResponseShort;
import com.techpeak.hac.sales.services.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {
    @Override
    public Long createQuotation(CreateQuotationRequest request, User user) {
        return null;
    }

    @Override
    public QuotationResponse getOne(Long id) {
        return null;
    }

    @Override
    public Page<QuotationResponseShort> search(int page, int size, String sort, Long ref, Long customer, Long user, String date, String rfpq) {
        return null;
    }

    @Override
    public Page<QuotationResponseShort> getAllActiveQuotations() {
        return null;
    }
}
