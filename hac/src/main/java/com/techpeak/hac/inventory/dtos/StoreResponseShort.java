package com.techpeak.hac.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseShort implements Serializable {
    private Long id;
    private String nameAr;
    private String nameEn;
}
