package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.inventory.models.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public MainBrand(Long id, String nameAr, String nameEn, String code, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.code = code;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
