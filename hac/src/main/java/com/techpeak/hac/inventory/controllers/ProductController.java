package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.ProductRequest;
import com.techpeak.hac.inventory.dtos.ProductResponse;
import com.techpeak.hac.inventory.models.Alternative;
import com.techpeak.hac.inventory.services.AlternativesServices;
import com.techpeak.hac.inventory.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;
  private final AlternativesServices alternativesServices;

  @PostMapping
  public ResponseEntity<Long> create(@RequestBody ProductRequest productRequest){
      Long id = productService.create(productRequest);
      return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<ProductResponse>> list(
          @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "80") Integer size,
          @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive, @RequestParam(value = "name", defaultValue = "") String name
  ) {
      Pageable pageRequest = PageRequest.of(page, size);
      Page<ProductResponse> data = productService.listWithPages(pageRequest, isActive, name);
      return ResponseEntity.ok(data);
  }

  @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(productService.get(id));
    }

    @GetMapping("/{id}/alternatives")
    public ResponseEntity<List<Alternative>> listProductAlternatives(@PathVariable Long id){
        ProductResponse productResponse = productService.get(id);
        List<Alternative> alternatives = alternativesServices.listByProductNumber(productResponse.getProductNumber());
        return ResponseEntity.ok(alternatives);
    }
}
