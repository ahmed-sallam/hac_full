package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.inventory.mappers.ProductMapper;
import com.techpeak.hac.inventory.models.Product;
import com.techpeak.hac.purchase.dtos.SupplierQuotationLineDto;
import com.techpeak.hac.purchase.dtos.SupplierQuotationLineRequest;
import com.techpeak.hac.purchase.models.SupplierQuotationLine;

public class SupplierQuotationLineMapper {

    private SupplierQuotationLineMapper() {
    }

    public static SupplierQuotationLine mapToSupplierQuotationLine(SupplierQuotationLineRequest request, Product product) {
        return SupplierQuotationLine.builder()
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .discount(request.getDiscount())
//                .vat(request.getVat())
//                .subTotal(request.getSubTotal())
                .total(request.getTotal())
                .product(product)
                .notes(request.getNotes())
                .build();
    }

    public static SupplierQuotationLineDto mapToDto (SupplierQuotationLine supplierQuotationLine) {
        return SupplierQuotationLineDto.builder()
                .id(supplierQuotationLine.getId())
                .quantity(supplierQuotationLine.getQuantity())
                .price(supplierQuotationLine.getPrice())
                .discount(supplierQuotationLine.getDiscount())
                .total(supplierQuotationLine.getTotal())
                .notes(supplierQuotationLine.getNotes())
                .product(ProductMapper.mapToProductResponseShort(supplierQuotationLine.getProduct()))
                .build();
    }
}
