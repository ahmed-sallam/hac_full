package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.core.mappers.UserMapper;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.SupplierQuotationRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponse;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponseShort;
import com.techpeak.hac.purchase.models.RFPQ;
import com.techpeak.hac.purchase.models.Supplier;
import com.techpeak.hac.purchase.models.SupplierQuotation;

public class SupplierQuotationMapper {
    private SupplierQuotationMapper() {
    }

    public static SupplierQuotation mapToSupplierQuotation(
            SupplierQuotationRequest request,
            User user, CurrencyEntity currency,
            InternalRef internalRef, RFPQ rfpq, Supplier supplier) {
        // todo calc lines
        return SupplierQuotation.builder()
                .notes(request.getNotes())
                .date(request.getDate())
                .validTo(request.getValidTo())
                .receiveIn(request.getReceiveIn())
                .currency(currency)
                .subTotal(request.getSubTotal())
                .discount(request.getDiscount())
                .vat(request.getVat())
                .totalExpenses(request.getTotalExpenses())
                .total(request.getTotal())
                .isLocal(request.getIsLocal())
                .paymentTerms(request.getPaymentTerms())
                .supplierRef(request.getSupplierRef())
                .internalRef(internalRef)
                .user(user)
                .rfpq(rfpq)
                .supplier(supplier)
//                .expenses(request.getExpenses().stream().map(SupplierQuotationExpensesMapper::mapToSupplierQuotationExpenses).collect(Collectors.toSet()))
//                .lines(request.getLines().stream().map((l)->SupplierQuotationLineMapper.mapToSupplierQuotationLine(l)).collect(Collectors.toSet()))
                .build();
    }


    public static SupplierQuotationResponseShort mapToResponseShort(SupplierQuotation supplierQuotation) {
        return SupplierQuotationResponseShort.builder()
                .id(supplierQuotation.getId())
                .isActive(supplierQuotation.getIsActive())
                .date(supplierQuotation.getDate())
                .currency(CurrencyMapper.mapToCurrencyCodeDto(supplierQuotation.getCurrency()))
                .total(supplierQuotation.getTotal())
                .isLocal(supplierQuotation.getIsLocal())
                .paymentTerms(supplierQuotation.getPaymentTerms())
                .supplierRef(supplierQuotation.getSupplierRef())
                .internalRef(InternalRefMapper.mapToInternalRefDto(supplierQuotation.getInternalRef()))
                .user(UserMapper.toDtoShort(supplierQuotation.getUser()))
                .rfpq(RFPQMapper.mapToRFPQResponseNumber(supplierQuotation.getRfpq()))
                .supplier(SupplierMapper.mapToSupplierResponseName(supplierQuotation.getSupplier()))
                .build();
    }


    public static SupplierQuotationResponse mapToResponse(SupplierQuotation supplierQuotation) {
        return SupplierQuotationResponse.builder()
                .id(supplierQuotation.getId())
                .createdAt(supplierQuotation.getCreatedAt())
                .updatedAt(supplierQuotation.getUpdatedAt())
                .isActive(supplierQuotation.getIsActive())
                .notes(supplierQuotation.getNotes())
                .date(supplierQuotation.getDate())
                .validTo(supplierQuotation.getValidTo())
                .receiveIn(supplierQuotation.getReceiveIn())
                .currency(CurrencyMapper.mapToCurrencyCodeDto(supplierQuotation.getCurrency()))
                .subTotal(supplierQuotation.getSubTotal())
                .discount(supplierQuotation.getDiscount())
                .vat(supplierQuotation.getVat())
                .totalExpenses(supplierQuotation.getTotalExpenses())
                .total(supplierQuotation.getTotal())
                .isLocal(supplierQuotation.getIsLocal())
                .paymentTerms(supplierQuotation.getPaymentTerms())
                .supplierRef(supplierQuotation.getSupplierRef())
                .internalRef(InternalRefMapper.mapToInternalRefDto(supplierQuotation.getInternalRef()))
                .user(UserMapper.toDtoShort(supplierQuotation.getUser()))
                .rfpq(RFPQMapper.mapToRFPQResponseNumber(supplierQuotation.getRfpq()))
                .supplier(SupplierMapper.mapToSupplierResponseName(supplierQuotation.getSupplier()))
//                .expenses(supplierQuotation.getExpenses().stream().map(PurchaseExpensesMapper::mapToDto).collect(Collectors.toSet()))
//                .lines(supplierQuotation.getLines().stream().map(SupplierQuotationLineMapper::mapToDto).collect(Collectors.toSet()))
                .build();
    }

}
