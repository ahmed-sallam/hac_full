package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.services.MaterialRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(path="/api/v1/material_requests")
@RequiredArgsConstructor
@Validated
public class MaterialRequestController {
private final MaterialRequestService materialRequestService;
    @PostMapping
    public ResponseEntity<Resource> create(@Valid @RequestBody CreateMaterialRequest createMaterialRequest){

        MaterialRequest materialRequest = materialRequestService.create(createMaterialRequest);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(materialRequest.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Resource> updateStatus(@RequestBody RequestStatus status, @PathVariable("id") Long id){
        materialRequestService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
