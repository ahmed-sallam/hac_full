package com.techpeak.hac.inventory.mappers;

import com.techpeak.hac.inventory.dtos.ProductRequest;
import com.techpeak.hac.inventory.dtos.ProductResponse;
import com.techpeak.hac.inventory.dtos.ProductResponseShort;
import com.techpeak.hac.inventory.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductRequest dto) {
        Product product = new Product();
        product.setProductNumber(dto.getProductNumber());
        product.setDescriptionAr(dto.getDescriptionAr());
        product.setDescriptionEn(dto.getDescriptionEn());
        product.setImage(dto.getImage());
        product.setPartImage(dto.getPartImage());
        product.setMinQuantity(dto.getMinQuantity());
        product.setSellQuantity(dto.getSellQuantity());
        product.setSellIndividual(dto.getSellIndividual());
        product.setBuyQuantity(dto.getBuyQuantity());
        product.setBuyIndividual(dto.getBuyIndividual());
        product.setIsOriginal(dto.getIsOriginal());
        product.setUnit(dto.getUnit());
        // Note: You need to fetch the MachinePart, Brand, and Country entities from their respective repositories using the provided ids
        return product;
    }


    public static  ProductResponse toDto(Product product) {
        ProductResponse response = new ProductResponse(

        );

        response.setId(product.getId());
        response.setProductNumber(product.getProductNumber());
        response.setDescriptionAr(product.getDescriptionAr());
        response.setDescriptionEn(product.getDescriptionEn());
        response.setImage(product.getImage());
        response.setPartImage(product.getPartImage());
        response.setMinQuantity(product.getMinQuantity());
        response.setSellQuantity(product.getSellQuantity());
        response.setSellIndividual(product.getSellIndividual());
        response.setBuyQuantity(product.getBuyQuantity());
        response.setBuyIndividual(product.getBuyIndividual());
        response.setIsOriginal(product.getIsOriginal());
        response.setUnit(product.getUnit());
        response.setMachinePart(product.getMachinePart());
        response.setMainBrand(product.getMainBrand());
        response.setSubBrand(product.getSubBrand());
        response.setMachineryType(product.getMachineryType());
        response.setMachineryModel(product.getMachineryModel());
        response.setCountry(product.getCountry());
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        // Note: You need to fetch the ProductSet entities from the ProductSetRepository using the provided product id
        return response;
    }
    public static  ProductResponse toDtoWithTotalInventory(Object[] productL) {
        ProductResponse response = new ProductResponse(

        );
        System.out.println("aaaaa" + productL[0]);
        Product product = (Product) productL[0];
        Long totalInventory = (Long) productL[1];
        response.setId(product.getId());
        response.setProductNumber(product.getProductNumber());
        response.setDescriptionAr(product.getDescriptionAr());
        response.setDescriptionEn(product.getDescriptionEn());
        response.setImage(product.getImage());
        response.setPartImage(product.getPartImage());
        response.setMinQuantity(product.getMinQuantity());
        response.setSellQuantity(product.getSellQuantity());
        response.setSellIndividual(product.getSellIndividual());
        response.setBuyQuantity(product.getBuyQuantity());
        response.setBuyIndividual(product.getBuyIndividual());
        response.setIsOriginal(product.getIsOriginal());
        response.setUnit(product.getUnit());
        response.setMachinePart(product.getMachinePart());
        response.setMainBrand(product.getMainBrand());
        response.setSubBrand(product.getSubBrand());
        response.setMachineryType(product.getMachineryType());
        response.setMachineryModel(product.getMachineryModel());
        response.setCountry(product.getCountry());
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        // Note: You need to fetch the ProductSet entities from the ProductSetRepository using the provided product id
        response.setTotalInventory(totalInventory);
        return response;
    }

    public static ProductResponseShort mapToProductResponseShort(Product product){
        return ProductResponseShort.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .descriptionAr(product.getDescriptionAr())
                .descriptionEn(product.getDescriptionEn())
                .mainBrandAr(product.getMainBrand().getNameAr())
                .mainBrandEn(product.getMainBrand().getNameEn())
                .subBrandAr(product.getSubBrand().getNameAr())
                .subBrandEn(product.getSubBrand().getNameEn())
                .build();
    }

}