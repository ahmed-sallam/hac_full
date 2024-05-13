package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.services.CountryService;
import com.techpeak.hac.inventory.dtos.*;
import com.techpeak.hac.inventory.enums.ProductUnit;
import com.techpeak.hac.inventory.mappers.ProductMapper;
import com.techpeak.hac.inventory.models.Alternative;
import com.techpeak.hac.inventory.models.Product;
import com.techpeak.hac.inventory.models.ProductSet;
import com.techpeak.hac.inventory.models.Related;
import com.techpeak.hac.inventory.repositories.ProductRepository;
import com.techpeak.hac.inventory.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MachinePartService machinePartService;
    private final BrandService brandService;
    private final CountryService countryService;
    private final ProductSetService productSetService;
    private final AlternativesServices alternativesServices;
    private final RelatedServices relatedServices;
    private final MachineryTypeService machineryTypeService;
    private final MachineryModelService machineryModelService;

    @Override
    @Transactional
    public Long create(ProductRequest productRequest) {
        boolean isSet = productRequest.getUnit().equals(ProductUnit.SET);
        List<Map<String, String>> alternatives = productRequest.getAlternatives();
        List<CreateRelated> relateds = productRequest.getRelated();
        Product entity = ProductMapper.toEntity(productRequest);
        entity.setMachinePart(machinePartService.getMachinePartOrThrow(productRequest.getMachinePart()));
        entity.setMainBrand(brandService.getBrandOrThrow(productRequest.getMainBrand()));
        entity.setSubBrand(brandService.getBrandOrThrow(productRequest.getSubBrand()));
        entity.setCountry(countryService.getCountryOrThrow(productRequest.getCountry()));
        entity.setMachineryType(machineryTypeService.getOrElseThrow(productRequest.getMachineryType()));
        entity.setMachineryModel((machineryModelService.getOrElseThrow(productRequest.getMachineryModel())));
        Product saved = productRepository.save(entity);

        if (isSet) {
            if (productRequest.getProductSets().size() <= 1) {
                throw new RuntimeException("Product set must have at least 2 products");
                // todo create custom exception
            }
            for (CreateProductSet productSet : productRequest.getProductSets()) {
                productSet.setProductSetId(saved.getId());
                productSetService.craete(productSet);
            }
        }
        if (!alternatives.isEmpty()) {
            for (Map<String, String> alternative : alternatives) {
                alternativesServices
                        .create(
                                new CreateAlternative(entity.getProductNumber(),
                                        alternative.get("product2Number")))
                ;
            }
        }
        if (!relateds.isEmpty()) {
            for (CreateRelated related : relateds) {
                relatedServices
                        .create(new CreateRelated(entity.getProductNumber(), related.getProduct2Number(), related.getIsRestricted()));
            }
        }
        return saved.getId();
    }

    @Override
    public Page<ProductResponse> listWithPages(Pageable pageRequest, Boolean isActive, String number) {
        Page<Object[]> all = productRepository.findByIsActiveAndProductNumberContainingIgnoreCase(isActive, number, pageRequest);
        return all.map(ProductMapper::toDtoWithTotalInventory);

    }

    @Override
    public List<ProductResponse> list(Boolean isActive, String number, Boolean exactMatch) {
        List<Product> all =
                exactMatch
                        ? productRepository.findByIsActiveAndProductNumberContainingIgnoreCase(isActive, number)
                        : productRepository.searchByProductNumber(isActive, number);
        return all.stream().map(ProductMapper::toDto).toList();
    }
    @Override
    public List<ProductResponse> listWithTotalInventory(Boolean isActive, String number) {
        List<Object[]> all =
                 productRepository.findByIsActiveAndProductNumberContainingIgnoreCaseWithTotalInventory(isActive, number);

        return all.stream().map(ProductMapper::toDtoWithTotalInventory).toList();
    }

    @Override
    public Product getProductOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id " + id + " does not exist"));

    }

    @Override
    @Transactional
    public   ProductResponse get(Long id) {
        Product product = getProductOrThrow(id);
        List<Object[]> o = this.productRepository.findProductWithTotalInventory(id);
        ProductResponse dto = ProductMapper.toDtoWithTotalInventory(o.get(0));
//        Set<ProductsSetResponse> productsSetResponses = product.getSetItems().stream().map(ProductSetMapper::toDto).collect(Collectors.toSet());
        List<ProductSetItemResponse> setProductsResponse = getProductSetItemResponses(product);
        dto.setSetItems(setProductsResponse);
        List<Alternative> alternatives = alternativesServices.listByProductNumber(product.getProductNumber());
        List<ProductResponse> alternativesProducts = alternatives.stream().filter(a -> !a.getProduct1Number().equals(product.getProductNumber()))
                .map(a -> listWithTotalInventory(true, a.getProduct1Number())).flatMap(List::stream).collect(Collectors.toList());
        alternativesProducts.addAll(alternatives.stream().filter(a -> !a.getProduct2Number().equals(product.getProductNumber()))
                .map(a -> listWithTotalInventory(true, a.getProduct2Number())).flatMap(List::stream).toList());
        dto.setAlternatives(alternativesProducts);
        List<Related> relateds = relatedServices.listByProductNumber(product.getProductNumber());
        List<ProductResponse> relatedProducts = relateds.stream().filter(a -> !a.getProduct1Number().equals(product.getProductNumber()))
                .map(a -> listWithTotalInventory(true, a.getProduct1Number())).flatMap(List::stream).collect(Collectors.toList());
        relatedProducts.addAll(relateds.stream().filter(a -> !a.getProduct2Number().equals(product.getProductNumber()))
                .map(a -> listWithTotalInventory(true, a.getProduct2Number())).flatMap(List::stream).toList());
        dto.setRelated(relatedProducts);
        dto.setSameItems(listWithTotalInventory(true, product.getProductNumber()).stream().filter(p -> !p.getId().equals(id)).toList());
        return dto;
    }

    private List<ProductSetItemResponse> getProductSetItemResponses(Product product) {
        List<ProductSet> setItems = new ArrayList<>(product.getSetItems());
        List<ProductResponse> setProducts = setItems.stream()
                .map(ProductSet::getProduct).map(ProductMapper::toDto).toList();
        // cat setProducts to ProductSetItemResponse
        List<ProductSetItemResponse> setProductsResponse = IntStream.range(0, setProducts.size())
                .mapToObj(index -> {
                    ProductResponse productResponse = setProducts.get(index);
                    ProductSetItemResponse productSetItemResponse = new ProductSetItemResponse();
                    productSetItemResponse.setId(productResponse.getId());
                    productSetItemResponse.setProductNumber(productResponse.getProductNumber());
                    productSetItemResponse.setDescriptionAr(productResponse.getDescriptionAr());
                    productSetItemResponse.setDescriptionEn(productResponse.getDescriptionEn());
                    productSetItemResponse.setImage(productResponse.getImage());
                    productSetItemResponse.setPartImage(productResponse.getPartImage());
                    productSetItemResponse.setMinQuantity(productResponse.getMinQuantity());
                    productSetItemResponse.setIsOriginal(productResponse.getIsOriginal());
                    productSetItemResponse.setUnit(productResponse.getUnit());
                    productSetItemResponse.setMachinePart(productResponse.getMachinePart());
                    productSetItemResponse.setMainBrand(productResponse.getMainBrand());
                    productSetItemResponse.setSubBrand(productResponse.getSubBrand());
                    productSetItemResponse.setCountry(productResponse.getCountry());
                    productSetItemResponse.setIsActive(productResponse.getIsActive());
                    productSetItemResponse.setCreatedAt(productResponse.getCreatedAt());
                    productSetItemResponse.setUpdatedAt(productResponse.getUpdatedAt());
                    productSetItemResponse.setSetId(setItems.get(index).getId());
                    productSetItemResponse.setQuantity(setItems.get(index).getQuantity());
                    return productSetItemResponse;
                })
                .collect(Collectors.toList());
        return setProductsResponse;
    }
}
