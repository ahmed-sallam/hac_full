package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.purchase.dtos.CreateSupplier;
import com.techpeak.hac.purchase.dtos.SupplierResponse;
import com.techpeak.hac.purchase.mappers.SupplierMapper;
import com.techpeak.hac.purchase.models.Supplier;
import com.techpeak.hac.purchase.repositories.SupplierRepository;
import com.techpeak.hac.purchase.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    @Override
    public Supplier create(CreateSupplier createSupplier) {
        Supplier supplier = SupplierMapper.dtoToSupplier(createSupplier);
        return supplierRepository.save(supplier);
    }



    @Override
    public Page<SupplierResponse> search(Pageable pageRequest, String search, Boolean isActive) {
        Page<Supplier> all = supplierRepository.findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCase(isActive, search, search, pageRequest);
        return all.map(SupplierMapper::entityToSupplierResponse);
    }

}
