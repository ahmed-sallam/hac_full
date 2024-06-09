package com.techpeak.hac.inventory.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ProductResponseShort implements Serializable {
    private  Long id;
    private String productNumber;
    private String descriptionAr;
    private String descriptionEn;
    private String mainBrandAr;
    private String mainBrandEn;
    private String subBrandAr;
    private String subBrandEn;
    private int totalInventory;
}
