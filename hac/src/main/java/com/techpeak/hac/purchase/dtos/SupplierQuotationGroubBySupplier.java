package com.techpeak.hac.purchase.dtos;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;


@Builder
public record SupplierQuotationGroubBySupplier (
        Long id,
        String nameAr,
        String nameEn,
        List<ProductQuotationDto> quotations
        ) implements Serializable {}