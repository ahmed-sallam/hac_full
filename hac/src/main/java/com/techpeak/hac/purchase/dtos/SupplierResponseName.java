package com.techpeak.hac.purchase.dtos;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierResponseName implements Serializable {
    Long id;
    String nameAr;
    String nameEn;
}