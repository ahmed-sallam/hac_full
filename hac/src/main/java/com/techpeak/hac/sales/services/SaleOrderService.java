package com.techpeak.hac.sales.services;

import org.springframework.data.domain.Page;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.sales.dtos.CreateSaleOrder;
import com.techpeak.hac.sales.dtos.SaleOrderResponse;
import com.techpeak.hac.sales.dtos.SaleOrderResponseShort;
import com.techpeak.hac.sales.models.Quotation;
import com.techpeak.hac.sales.models.SaleOrder;

public interface SaleOrderService {
    Long createSaleOrder(CreateSaleOrder request, User user);

    SaleOrderResponse getOne(Long id);

    Page<SaleOrderResponseShort> search(int page, int size, String sort, Long ref, Long customer, Long user,
            String date, String order);

    Page<SaleOrderResponseShort> getAllActiveSaleOrders();

    SaleOrder createInternal(Quotation quotation, User user);
}