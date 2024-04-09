package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateBrand;
import com.techpeak.hac.inventory.dtos.MainBrand;
import com.techpeak.hac.inventory.models.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    void createBrand(CreateBrand createBrand);
    void updateBrand(Long id,CreateBrand updateBrand);
    MainBrand getBrand(Long id);
    Page<MainBrand> list(Pageable pageable, Boolean isActive, String name);

    Brand getBrandOrThrow(Long id);
}
