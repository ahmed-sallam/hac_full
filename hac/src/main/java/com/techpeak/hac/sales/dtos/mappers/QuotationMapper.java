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
import com.techpeak.hac.sales.dtos.CreateQuotationRequest;
import com.techpeak.hac.sales.dtos.CreateQuotationRequestLine;
import com.techpeak.hac.sales.dtos.CustomerResponseName;
import com.techpeak.hac.sales.dtos.QuotationLineDto;
import com.techpeak.hac.sales.dtos.QuotationResponse;
import com.techpeak.hac.sales.dtos.QuotationResponseShort;
import com.techpeak.hac.sales.models.Customer;
import com.techpeak.hac.sales.models.Quotation;
import com.techpeak.hac.sales.models.QuotationLine;

public class QuotationMapper {

    public static Quotation toQuotation(CreateQuotationRequest request,
            Customer customer,
            CurrencyEntity currency, User user) {
        if (request == null) {
            return null;
        }

        Quotation quotation = new Quotation();
        quotation.setNotes(request.getNotes());
        quotation.setValidUntil(request.getValidUntil());
        quotation.setCustomer(customer);
        quotation.setCurrency(currency);
        quotation.setPaymentTerms(request.getPaymentTerms());
        quotation.setStatus(request.getStatus());
        quotation.setUser(user);
        // quotation.setLines(request.getLines().stream()
        // .map(QuotationMapper::toQuotationLine)
        // .collect(Collectors.toSet()));
        return quotation;
    }

    public static QuotationLine toQuotationLine(CreateQuotationRequestLine requestLine, Product product) {
        if (requestLine == null) {
            return null;
        }

        QuotationLine line = new QuotationLine();
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

    public static QuotationResponse toQuotationResponse(Quotation quotation) {
        if (quotation == null) {
            return null;
        }

        return QuotationResponse.builder()
                .id(quotation.getId())
                .createdAt(quotation.getCreatedAt())
                .updatedAt(quotation.getUpdatedAt())
                .isActive(quotation.getIsActive())
                .status(quotation.getStatus())
                .number(quotation.getNumber())
                .subTotal(quotation.getSubTotal())
                .discount(quotation.getDiscount())
                .vat(quotation.getVat())
                .total(quotation.getTotal())
                .notes(quotation.getNotes())
                .date(quotation.getDate())
                .validUntil(quotation.getValidUntil())
                .paymentTerms(quotation.getPaymentTerms())
                .internalRef(InternalRefMapper.mapToInternalRefDto(quotation.getInternalRef()))
                .user(UserMapper.toDtoShort(quotation.getUser()))
                .customer(CustomerMapper.entityToCustomerResponse(quotation.getCustomer()))
                .lines(quotation.getLines().stream()
                        .map(QuotationMapper::toQuotationLineDto)
                        .collect(Collectors.toSet()))
                // .userHistories(quotation.getUserHistories().stream()
                // .map(QuotationMapper::toUserHistoryResponse)
                // .collect(Collectors.toSet()))
                .build();
    }

    public static QuotationResponseShort toQuotationResponseShort(Quotation quotation) {
        if (quotation == null) {
            return null;
        }

        return QuotationResponseShort.builder()
                .id(quotation.getId())
                .isActive(quotation.getIsActive())
                .status(quotation.getStatus())
                .number(quotation.getNumber())
                .total(quotation.getTotal())
                .date(quotation.getDate())
                .paymentTerms(quotation.getPaymentTerms())
                .internalRef(InternalRefMapper.mapToInternalRefDto(quotation.getInternalRef()))
                .user(UserMapper.toDtoShort(quotation.getUser()))
                .customer(toCustomerResponseName(quotation.getCustomer()))
                .build();
    }

    // Helper methods to convert nested objects
    // private static InternalRefDto toInternalRefDto(InternalRef internalRef) {
    // if (internalRef == null) {
    // return null;
    // }
    // return new InternalRefDto(internalRef.getId(), internalRef.getReference());
    // }

    // private static CustomerResponse toCustomerResponse(Customer customer) {
    // if (customer == null) {
    // return null;
    // }
    // return new CustomerResponse(customer.getId(), customer.getNameAr(),
    // customer.getNameEn());
    // }

    private static CustomerResponseName toCustomerResponseName(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponseName(customer.getId(), customer.getNameAr(), customer.getNameEn());
    }

    private static QuotationLineDto toQuotationLineDto(QuotationLine line) {
        if (line == null) {
            return null;
        }
        return QuotationLineDto.builder()
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
