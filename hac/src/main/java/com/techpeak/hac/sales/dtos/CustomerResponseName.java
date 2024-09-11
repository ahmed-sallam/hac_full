package com.techpeak.hac.sales.dtos;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponseName implements Serializable {
    Long id;
    String nameAr;
    String nameEn;
}
