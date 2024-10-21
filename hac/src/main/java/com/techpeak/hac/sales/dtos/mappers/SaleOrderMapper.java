package com.techpeak.hac.sales.dtos.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.inventory.mappers.ProductMapper;
import com.techpeak.hac.inventory.models.Product;
import com.techpeak.hac.purchase.mappers.InternalRefMapper;
import com.techpeak.hac.sales.dtos.CreateSaleOrder;
import com.techpeak.hac.sales.dtos.CreateSaleOrderLine;
import com.techpeak.hac.sales.dtos.CustomerResponseName;
import com.techpeak.hac.sales.dtos.SaleOrderLineDto;
import com.techpeak.hac.sales.dtos.SaleOrderResponse;
import com.techpeak.hac.sales.dtos.SaleOrderResponseShort;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.models.SaleOrder;
import com.techpeak.hac.sales.models.SaleOrderLine;

public class SaleOrderMapper {

    public static SaleOrder toSaleOrder(CreateSaleOrder request,
            Customer customer,
            CurrencyEntity currency, User user) {
        if (request == null) {
            return null;
        }

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setNotes(request.getNotes());
        saleOrder.setDeliveryDate(request.getDeliveryDate());
        saleOrder.setCustomer(customer);
        saleOrder.setCurrency(currency);
        saleOrder.setPaymentTerms(request.getPaymentTerms());
        saleOrder.setStatus(request.getStatus());
        saleOrder.setUser(user);
        return saleOrder;
    }

    public static SaleOrderLine toSaleOrderLine(CreateSaleOrderLine requestLine, Product product) {
        if (requestLine == null) {
            return null;
        }

        SaleOrderLine line = new SaleOrderLine();
        line.setQuantity(requestLine.getQuantity());
        line.setPrice(requestLine.getPrice());
        line.setDiscount(requestLine.getDiscount());
        line.setNotes(requestLine.getNotes());
        line.setProduct(product);
        BigDecimal discountAmount = requestLine.getPrice()
                .multiply(requestLine.getDiscount())
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal discountedPrice = requestLine.getPrice().subtract(discountAmount);
        BigDecimal lineTotal = discountedPrice.multiply(BigDecimal.valueOf(requestLine.getQuantity()));
        line.setLineTotal(lineTotal);
        return line;
    }

    public static SaleOrderResponse toSaleOrderResponse(SaleOrder saleOrder) {
        if (saleOrder == null) {
            return null;
        }

        return SaleOrderResponse.builder()
                .id(saleOrder.getId())
                .createdAt(saleOrder.getCreatedAt())
                .updatedAt(saleOrder.getUpdatedAt())
                .isActive(saleOrder.getIsActive())
                .status(saleOrder.getStatus())
                .number(saleOrder.getNumber())
                .subTotal(saleOrder.getSubTotal())
                .discount(saleOrder.getDiscount())
                .vat(saleOrder.getVat())
                .total(saleOrder.getTotal())
                .notes(saleOrder.getNotes())
                .date(saleOrder.getDate())
                .deliveryDate(saleOrder.getDeliveryDate())
                .paymentTerms(saleOrder.getPaymentTerms())
                .internalRef(InternalRefMapper.mapToInternalRefDto(saleOrder.getInternalRef()))
                .user(UserMapper.toDtoShort(saleOrder.getUser()))
                .customer(CustomerMapper.entityToCustomerResponse(saleOrder.getCustomer()))
                .lines(saleOrder.getLines().stream()
                        .map(SaleOrderMapper::toSaleOrderLineDto)
                        .collect(Collectors.toSet()))
                .userHistories(saleOrder.getUserHistories().stream()
                        .map(SaleOrderMapper::toUserHistoryResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static SaleOrderResponseShort toSaleOrderResponseShort(SaleOrder saleOrder) {
        if (saleOrder == null) {
            return null;
        }

        return SaleOrderResponseShort.builder()
                .id(saleOrder.getId())
                .isActive(saleOrder.getIsActive())
                .status(saleOrder.getStatus())
                .number(saleOrder.getNumber())
                .total(saleOrder.getTotal())
                .date(saleOrder.getDate())
                .paymentTerms(saleOrder.getPaymentTerms())
                .internalRef(InternalRefMapper.mapToInternalRefDto(saleOrder.getInternalRef()))
                .user(UserMapper.toDtoShort(saleOrder.getUser()))
                .customer(toCustomerResponseName(saleOrder.getCustomer()))
                .build();
    }

    private static CustomerResponseName toCustomerResponseName(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponseName(customer.getId(), customer.getNameAr(), customer.getNameEn());
    }

    private static SaleOrderLineDto toSaleOrderLineDto(SaleOrderLine line) {
        if (line == null) {
            return null;
        }
        return SaleOrderLineDto.builder()
                .id(line.getId())
                .createdAt(line.getCreatedAt())
                .updatedAt(line.getUpdatedAt())
                .isActive(line.getIsActive())
                .quantity(line.getQuantity())
                .price(line.getPrice())
                .discount(line.getDiscount())
                .total(line.getLineTotal())
                .notes(line.getNotes())
                .product(ProductMapper.mapToProductResponseShort(line.getProduct()))
                .build();
    }

    private static UserHistoryResponse toUserHistoryResponse(UserHistory userHistory) {
        if (userHistory == null) {
            return null;
        }
        return new UserHistoryResponse(
                userHistory.getId(),
                userHistory.getActionDetails(),
                userHistory.getTableName(),
                userHistory.getRecordId(),
                userHistory.getDateTime(),
                UserMapper.toDtoShort(userHistory.getUser()));
    }
}