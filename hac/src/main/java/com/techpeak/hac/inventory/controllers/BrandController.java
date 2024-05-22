package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.CreateBrand;
import com.techpeak.hac.inventory.dtos.MainBrand;
import com.techpeak.hac.inventory.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
@Validated
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<String> createBrand(@RequestBody CreateBrand createBrand) {
        brandService.createBrand(createBrand);
        return ResponseEntity.created(URI.create("")).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBrand(@PathVariable("id") Long id, @RequestBody CreateBrand updateBrand) {
        brandService.updateBrand(id, updateBrand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainBrand> getBrand(@PathVariable("id") Long id) {
        MainBrand brand = brandService.getBrand(id);
        return ResponseEntity.ok(brand);
    }

    @GetMapping
    public ResponseEntity<Page<MainBrand>> list(
            @RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive, @RequestParam(value = "name", defaultValue = "") String name
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<MainBrand> data = brandService.list(pageRequest, isActive, name);
        return ResponseEntity.ok(data);
    }
}
