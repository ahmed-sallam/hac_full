package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.inventory.dtos.ProductResponseShort;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.purchase.models.SupplierQuotationLine}
 */
public record ProductQuotationDto(Long id, Double price, Double discount,
                                  ProductResponseShort product,
                                  SupplierQuotationResponseShortX supplierQuotation) implements Serializable {
}