package com.techpeak.hac.inventory.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StoreLocationResponse {
    private Long id;
    private String nameAr;
    private String nameEn;
    private Long storeId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
