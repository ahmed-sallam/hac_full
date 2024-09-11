package com.techpeak.hac.sales.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.sales.dtos.CreateQuotationRequest;
import com.techpeak.hac.sales.dtos.QuotationResponse;
import org.springframework.data.domain.Page;

public interface QuotationService {
    Long createQuotation(CreateQuotationRequest request, User user);

    QuotationResponse getOne(Long id);

    Page<QuotationResponseShort> search(int page, int size, String sort, Long ref, Long customer, Long user, String date, String rfpq);

    Page<QuotationResponseShort> getAllActiveQuotations();


}
