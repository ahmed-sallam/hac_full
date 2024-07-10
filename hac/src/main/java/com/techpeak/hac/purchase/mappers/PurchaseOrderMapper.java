package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.inventory.mappers.ProductMapper;
import com.techpeak.hac.inventory.mappers.StoreMapper;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderDto;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderLineDto;
import com.techpeak.hac.purchase.dtos.purchase_order.PurchaseOrderShort;
import com.techpeak.hac.purchase.models.PurchaseOrder;
import com.techpeak.hac.purchase.models.PurchaseOrderLine;

import java.util.stream.Collectors;

public class PurchaseOrderMapper {

    public static PurchaseOrderDto toDto(PurchaseOrder purchaseOrder) {
        return PurchaseOrderDto.builder()
                .id(purchaseOrder.getId())
                .date(purchaseOrder.getDate())
                .notes(purchaseOrder.getNotes())
                .internalRefId(purchaseOrder.getInternalRef().getId())
                .currentPhase(purchaseOrder.getInternalRef().getCurrentPhase().name())
                .number(purchaseOrder.getNumber())
                .total(purchaseOrder.getTotal())
                .subTotal(purchaseOrder.getSubTotal())
                .vat(purchaseOrder.getVat())
                .status(purchaseOrder.getStatus().name())
                .store(StoreMapper.toShort(purchaseOrder.getStore()))
                .user(UserMapper.toDtoShort(purchaseOrder.getUser()))
                .supplier(SupplierMapper.mapToSupplierResponseName(purchaseOrder.getSupplier()))
                .lines(purchaseOrder.getLines().stream().map(PurchaseOrderMapper::toOrderLineDto).collect(Collectors.toSet()))
                .build();
    }

    public static PurchaseOrderLineDto toOrderLineDto(PurchaseOrderLine purchaseOrderLine) {
        return PurchaseOrderLineDto.builder()
                .id(purchaseOrderLine.getId())
                .quantity(purchaseOrderLine.getQuantity())
                .price(purchaseOrderLine.getPrice())
                .total(purchaseOrderLine.getTotal())
                .vat(purchaseOrderLine.getVat())
                .notes(purchaseOrderLine.getNotes())
                .product(ProductMapper.mapToProductResponseShort(purchaseOrderLine.getProduct()))
                .build();
    }

    public static PurchaseOrderShort toShort(PurchaseOrder purchaseOrder) {
        return PurchaseOrderShort.builder()
                .id(purchaseOrder.getId())
                .date(purchaseOrder.getDate())
                .internalRefId(purchaseOrder.getInternalRef().getId())
                .currentPhase(purchaseOrder.getInternalRef().getCurrentPhase().name())
                .number(purchaseOrder.getNumber())
                .total(purchaseOrder.getTotal())
                .status(purchaseOrder.getStatus().name())
                .store(StoreMapper.toShort(purchaseOrder.getStore()))
                .user(UserMapper.toDtoShort(purchaseOrder.getUser()))
                .supplier(SupplierMapper.mapToSupplierResponseName(purchaseOrder.getSupplier()))
                .build();
    }
}

