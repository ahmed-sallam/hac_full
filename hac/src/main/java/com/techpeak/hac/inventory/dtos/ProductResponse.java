package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.core.models.Country;
import com.techpeak.hac.inventory.enums.ProductUnit;
import com.techpeak.hac.inventory.models.Brand;
import com.techpeak.hac.inventory.models.MachinePart;
import com.techpeak.hac.inventory.models.MachineryModel;
import com.techpeak.hac.inventory.models.MachineryType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class ProductResponse {
    private  Long id;
    private String productNumber;
    private String descriptionAr;
    private String descriptionEn;
    private String image;
    private String partImage;
    private Integer minQuantity;
    private Integer sellQuantity;
    private Integer sellIndividual;
    private Integer buyQuantity;
    private Integer buyIndividual;
    private Boolean isOriginal;
    private ProductUnit unit;
    private MachinePart machinePart;
    private MachineryModel machineryModel;
    private MachineryType machineryType;
    private Brand mainBrand;
    private Brand subBrand;
    private Country country;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List< ProductSetItemResponse> setItems;
    private List<ProductResponse> sameItems;
    private List<ProductResponse> alternatives;
    private List<ProductResponse> related;
    private Long totalInventory;


}