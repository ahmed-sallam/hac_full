package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.inventory.dtos.CreateBrand;
import com.techpeak.hac.inventory.dtos.MainBrand;
import com.techpeak.hac.inventory.mappers.BrandMapper;
import com.techpeak.hac.inventory.models.Brand;
import com.techpeak.hac.inventory.repositories.BrandRepository;
import com.techpeak.hac.inventory.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public void createBrand(CreateBrand createBrand) {
        Brand brand = BrandMapper.toEntity(createBrand);
        if (createBrand.getBrandId()!= null)
            brand.setMainBrand(getBrandOrThrow(createBrand.getBrandId()).getId());
        brandRepository.save(brand);
    }

    @Override
    public void updateBrand(Long id, CreateBrand updateBrand) {
        Brand brand = getBrandOrThrow(id);
        brand.setNameAr(updateBrand.getNameAr());
        brand.setNameEn(updateBrand.getNameEn());
        brand.setCode(updateBrand.getCode());
        brand.setIsActive(updateBrand.getIsActive());
        if (updateBrand.getBrandId()!= null)
            brand.setMainBrand(getBrandOrThrow(updateBrand.getBrandId()).getId());
        brandRepository.save(brand);
    }



    @Override
    public MainBrand getBrand(Long id) {
        return BrandMapper.toDto(getBrandOrThrow(id));
    }

    @Override
    public Page<MainBrand> list(Pageable pageable, Boolean isActive, String name) {
        Page<Brand> all = brandRepository.findByIsActiveAndNameArContainingIgnoreCaseOrNameEnContainingIgnoreCaseOrCodeContainingIgnoreCase(isActive, name, name, name, pageable);
        return all.map(BrandMapper::toDto);
    }

    @Override
    public Brand getBrandOrThrow(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new NotFoundException("Brand with id " + id + " does not exist"));
    }
}
