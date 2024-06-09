package com.techpeak.hac.purchase.services;

import com.techpeak.hac.purchase.dtos.CreateSupplier;
import com.techpeak.hac.purchase.dtos.SupplierResponse;
import com.techpeak.hac.purchase.models.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    Supplier create(CreateSupplier createSupplier);

    Page<SupplierResponse> search(Pageable pageRequest, String search, Boolean isActive);
    Supplier get(Long id);
}
