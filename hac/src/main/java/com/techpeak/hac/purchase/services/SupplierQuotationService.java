package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.SupplierQuotationRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponseShort;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import org.springframework.data.domain.Page;

public interface SupplierQuotationService {
    Page<SupplierQuotationResponseShort> getAllSupplierQuotations();
    Page<SupplierQuotationResponseShort> getAllActiveSupplierQuotations();
    SupplierQuotation createSupplierQuotation(SupplierQuotationRequest request, User user);
}
