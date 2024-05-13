package com.techpeak.hac.inventory.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MachineryTypeResponse {
    private Long id;
    private String nameAr;
    private String nameEn;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MachineryModelResponse> machineryModels;
}
