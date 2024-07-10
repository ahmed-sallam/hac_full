package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.SupplierQuotationGroubBySupplier;
import com.techpeak.hac.purchase.dtos.SupplierQuotationRequest;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponse;
import com.techpeak.hac.purchase.dtos.SupplierQuotationResponseShort;
import com.techpeak.hac.purchase.dtos.bid_summary.GenerateBidSummaryDto;
import com.techpeak.hac.purchase.models.SupplierQuotation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface SupplierQuotationService {
    Page<SupplierQuotationResponseShort> search(int page, int size, String sort, Long ref, Long supplier, Long user, String supplierRef, Boolean isLocal, String date, String rfpq);
    Page<SupplierQuotationResponseShort> getAllActiveSupplierQuotations();
    SupplierQuotation createSupplierQuotation(SupplierQuotationRequest request, User user);

    SupplierQuotationResponse getOne(Long id);

    List<SupplierQuotationGroubBySupplier> getSupplierQuotationsGroupBySupplier( LocalDate fromDate,String productNumber,  List<String> numbers);

    GenerateBidSummaryDto getSupplierQuotationsGrouped(Long rfpq, LocalDate fromDate, LocalDate toDate);

    SupplierQuotation getSupplierQuotation(Long id);
}
