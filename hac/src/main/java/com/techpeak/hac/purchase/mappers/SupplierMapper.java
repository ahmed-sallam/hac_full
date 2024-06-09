package com.techpeak.hac.purchase.mappers;

import com.techpeak.hac.purchase.dtos.CreateSupplier;
import com.techpeak.hac.purchase.dtos.SupplierResponse;
import com.techpeak.hac.purchase.dtos.SupplierResponseName;
import com.techpeak.hac.purchase.models.Supplier;

public class SupplierMapper {
    private SupplierMapper() {
    }

    public static Supplier dtoToSupplier(CreateSupplier createSupplier) {
        Supplier supplier = Supplier.builder()
                .nameAr(createSupplier.getNameAr())
                .nameEn(createSupplier.getNameEn())
                .email(createSupplier.getEmail())
                .phone(createSupplier.getPhone())
                .address(createSupplier.getAddress())
                .build();
        supplier.setIsActive(createSupplier.getIsActive());
        return supplier;
    }

    public static SupplierResponse entityToSupplierResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .nameAr(supplier.getNameAr())
                .nameEn(supplier.getNameEn())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .isActive(supplier.getIsActive())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();

    }

    public static SupplierResponseName mapToSupplierResponseName(Supplier supplier){
        return SupplierResponseName.builder()
                .id(supplier.getId())
                .nameAr(supplier.getNameAr())
                .nameEn(supplier.getNameEn())
                .build();
    }
}
