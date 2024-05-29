package com.techpeak.hac.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseShort {
    private Long id;
    private String nameAr;
    private String nameEn;
}
