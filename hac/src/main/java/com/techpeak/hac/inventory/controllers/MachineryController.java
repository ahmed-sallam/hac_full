package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.*;
import com.techpeak.hac.inventory.models.MachineryType;
import com.techpeak.hac.inventory.services.MachineryModelService;
import com.techpeak.hac.inventory.services.MachineryTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/machinery")
@RequiredArgsConstructor
@Tag(name = "machinery")
public class MachineryController {
    private final MachineryTypeService machineryTypeService;
    private final MachineryModelService machineryModelService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateMachineryType createMachineryType) {
        machineryTypeService.create(createMachineryType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MachineryTypeResponse>> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "15") Integer size,
                                                    @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive,
                                                    @RequestParam(value = "name", defaultValue = "") String name
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(machineryTypeService.list(isActive, name, pageRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineryTypeResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(machineryTypeService.getMachineryType(id), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateMachineryType createMachineryType) {
        machineryTypeService.update(id, createMachineryType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{machineryTypeId}/models")
    public ResponseEntity<?> createModel(@PathVariable Long machineryTypeId, @RequestBody CreateMachineryModel createMachineryModel) {
        machineryModelService.create(machineryTypeId, createMachineryModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{machineryTypeId}/models/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable Long machineryTypeId, @PathVariable Long id, @RequestBody CreateMachineryModel createMachineryModel) {
        machineryModelService.update(id, createMachineryModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
