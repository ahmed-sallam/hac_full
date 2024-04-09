package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.CreateBrand;
import com.techpeak.hac.inventory.dtos.MainBrand;
import com.techpeak.hac.inventory.models.Brand;

public class BrandMapper {
    public static Brand toEntity(CreateBrand createBrand) {
        Brand brand = Brand
                .builder()
                .nameAr(createBrand.getNameAr())
                .nameEn(createBrand.getNameEn())
                .code(createBrand.getCode())
                .build();
        brand.setIsActive(createBrand.getIsActive());
        return brand;
    }

    public static MainBrand toDto(Brand brand) {
        return new MainBrand(
                brand.getId(),
                brand.getNameAr(),
                brand.getNameEn(),
                brand.getCode(),
                brand.getSubBrands(),
                brand.getIsActive(),
                brand.getCreatedAt(),
                brand.getUpdatedAt()
        );
    }

}
