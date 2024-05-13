package com.techpeak.hac.inventory.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MachineryModelResponse {
    private Long id;
    private String nameAr;
    private String nameEn;
    private MainBrand brand;
    private Long machineryType;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
