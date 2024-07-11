package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.purchase_order.CreatePurchaseOrder;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderDto;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.BidSummary;
import com.techpeak.hac.purchase.models.PurchaseOrder;
import org.springframework.data.domain.Page;

public interface PurchaseOrderService {

    PurchaseOrder createPurchaseOrder(CreatePurchaseOrder purchaseOrderDto);
    void createPurchaseOrderInternal(BidSummary bidSummary, User user);

    PurchaseOrderDto getPurchaseOrder(Long id);

    void updatePurchaseOrder(Long id, CreatePurchaseOrder purchaseOrderDto);

    Page<PurchaseOrderShort> getPurchaseOrders(int page, int size, String sort, String search, Long ref, Long store, Long user, String phase, String status, Long supplier);

    PurchaseOrder getOrThrow(Long id);

    void updatePurchaseOrderStatus(Long id, RequestStatus status, User user);
}
