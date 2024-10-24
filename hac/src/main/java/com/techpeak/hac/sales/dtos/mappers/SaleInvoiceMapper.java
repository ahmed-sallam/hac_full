package com.techpeak.hac.sales.dtos.mappers;

import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.models.UserHistory;
import com.techpeak.hac.inventory.mappers.ProductMapper;
import com.techpeak.hac.inventory.models.Product;
import com.techpeak.hac.purchase.mappers.InternalRefMapper;
import com.techpeak.hac.sales.dtos.*;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.models.SaleInvoice;
import com.techpeak.hac.sales.models.SaleInvoiceLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

public class SaleInvoiceMapper {

    public static SaleInvoice toSaleInvoice(CreateSaleInvoiceRequest request,
                                            Customer customer,
                                            CurrencyEntity currency, User user) {
        if (request == null) {
            return null;
        }

        SaleInvoice saleInvoice = new SaleInvoice();
        saleInvoice.setNotes(request.getNotes());
        saleInvoice.setDueDate(request.getDueDate());
        saleInvoice.setCustomer(customer);
        saleInvoice.setCurrency(currency);
        saleInvoice.setPaymentTerms(request.getPaymentTerms());
        saleInvoice.setStatus(request.getStatus());
        saleInvoice.setUser(user);
        return saleInvoice;
    }

    public static SaleInvoiceLine toSaleInvoiceLine(CreateSaleInvoiceRequestLine requestLine, Product product) {
        if (requestLine == null) {
            return null;
        }

        SaleInvoiceLine line = new SaleInvoiceLine();
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

    public static SaleInvoiceResponse toSaleInvoiceResponse(SaleInvoice saleInvoice) {
        if (saleInvoice == null) {
            return null;
        }

        return SaleInvoiceResponse.builder()
                .id(saleInvoice.getId())
                .createdAt(saleInvoice.getCreatedAt())
                .updatedAt(saleInvoice.getUpdatedAt())
                .isActive(saleInvoice.getIsActive())
                .status(saleInvoice.getStatus())
                .number(saleInvoice.getNumber())
                .subTotal(saleInvoice.getSubTotal())
                .discount(saleInvoice.getDiscount())
                .vat(saleInvoice.getVat())
                .total(saleInvoice.getTotal())
                .notes(saleInvoice.getNotes())
                .date(saleInvoice.getDate())
                .dueDate(saleInvoice.getDueDate())
                .paymentTerms(saleInvoice.getPaymentTerms())
                .internalRef(InternalRefMapper.mapToInternalRefDto(saleInvoice.getInternalRef()))
                .user(UserMapper.toDtoShort(saleInvoice.getUser()))
                .customer(CustomerMapper.entityToCustomerResponse(saleInvoice.getCustomer()))
                .lines(saleInvoice.getLines().stream()
                        .map(SaleInvoiceMapper::toSaleInvoiceLineDto)
                        .collect(Collectors.toSet()))
//                .userHistories(saleInvoice.getUserHistories().stream()
//                        .map(SaleInvoiceMapper::toUserHistoryResponse)
//                        .collect(Collectors.toSet()))
                .build();
    }

    public static SaleInvoiceResponseShort toSaleInvoiceResponseShort(SaleInvoice saleInvoice) {
        if (saleInvoice == null) {
            return null;
        }

        return SaleInvoiceResponseShort.builder()
                .id(saleInvoice.getId())
                .isActive(saleInvoice.getIsActive())
                .status(saleInvoice.getStatus())
                .number(saleInvoice.getNumber())
                .total(saleInvoice.getTotal())
                .date(saleInvoice.getDate())
                .paymentTerms(saleInvoice.getPaymentTerms())
                .internalRef(InternalRefMapper.mapToInternalRefDto(saleInvoice.getInternalRef()))
                .user(UserMapper.toDtoShort(saleInvoice.getUser()))
                .customer(toCustomerResponseName(saleInvoice.getCustomer()))
                .build();
    }

    private static CustomerResponseName toCustomerResponseName(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponseName(customer.getId(), customer.getNameAr(), customer.getNameEn());
    }

    private static SaleInvoiceLineDto toSaleInvoiceLineDto(SaleInvoiceLine line) {
        if (line == null) {
            return null;
        }
        return SaleInvoiceLineDto.builder()
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
