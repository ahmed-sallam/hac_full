package com.techpeak.hac.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryResponse {
    private Long id;
    private String nameAr;
    private String nameEn;
}
