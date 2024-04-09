package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.inventory.models.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class MainBrand {
    private Long id;
    private String nameAr;
    private String nameEn;
    private String code;
    private Set<Brand> subBrands;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
