package com.techpeak.hac.inventory.dtos;

import com.techpeak.hac.inventory.enums.ProductUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class ProductRequest {
    @NotNull
    @Size(max = 25)
    private String productNumber;

    @NotNull
    @Size(max = 255)
    private String descriptionAr;

    @NotNull
    @Size(max = 255)
    private String descriptionEn;

    @Size(max = 255)
    private String image;

    @Size(max = 255)
    private String partImage;

    @Min(0)
    private Integer minQuantity;

    @Min(0)
    private Integer sellQuantity;
    @Min(0)
    private Integer sellIndividual;
    @Min(0)
    private Integer buyQuantity;
    @Min(0)
    private Integer buyIndividual;

    @NotNull
    private Boolean isOriginal;

    @NotNull
    private ProductUnit unit;

    @NotNull
    private Long machinePart;

    @NotNull
    private Long mainBrand;

    @NotNull
    private Long subBrand;

    @NotNull
    private Long country;

    @NotNull
    private Long machineryType;
    @NotNull
    private Long machineryModel;


    private List<CreateProductSet> productSets;
    private List<Map<String, String>> alternatives;
    private List<CreateRelated> related;
}