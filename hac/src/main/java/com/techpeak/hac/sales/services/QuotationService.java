package com.techpeak.hac.sales.services;

import org.springframework.data.domain.Page;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.sales.dtos.CreateQuotationRequest;
import com.techpeak.hac.sales.dtos.QuotationResponse;
import com.techpeak.hac.sales.dtos.QuotationResponseShort;

public interface QuotationService {
    Long createQuotation(CreateQuotationRequest request, User user);

    QuotationResponse getOne(Long id);

    Page<QuotationResponseShort> search(int page, int size, String sort,
            Long ref, Long customer, Long user,
            String date, String quotation);

    Page<QuotationResponseShort> getAllActiveQuotations();

    void updateStatus(Long id, RequestStatus status, User user);

}
