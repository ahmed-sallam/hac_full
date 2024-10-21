package com.techpeak.hac.sales.services;

import org.springframework.data.domain.Page;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.sales.dtos.CreateSaleInvoiceRequest;
import com.techpeak.hac.sales.dtos.SaleInvoiceResponse;
import com.techpeak.hac.sales.dtos.SaleInvoiceResponseShort;

public interface SaleInvoiceService {
    Long createSaleInvoice(CreateSaleInvoiceRequest request, User user);

    SaleInvoiceResponse getOne(Long id);

    Page<SaleInvoiceResponseShort> search(int page, int size, String sort, Long ref, Long customer, Long user,
            String date, String invoice);

    Page<SaleInvoiceResponseShort> getAllActiveSaleInvoices();
}