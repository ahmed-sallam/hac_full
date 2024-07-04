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
public class ProductResponseXShort implements Serializable {
    private  Long id;
    private String productNumber;
    private String subBrandAr;
    private String subBrandEn;
    private Integer quantity;
    private Integer soldQuantity;
}
