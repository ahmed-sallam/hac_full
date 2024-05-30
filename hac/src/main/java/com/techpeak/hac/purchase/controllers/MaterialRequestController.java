package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponse;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponseShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.services.MaterialRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/material_requests")
@RequiredArgsConstructor
@Validated
public class MaterialRequestController {
    private final MaterialRequestService materialRequestService;

    @PostMapping
    public ResponseEntity<Long> create(
            @Valid @RequestBody CreateMaterialRequest createMaterialRequest) {

        User user = AuthUtils.getCurrentUser();
        MaterialRequest materialRequest = materialRequestService
                .create(createMaterialRequest, user);
//        URI uri = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(materialRequest.getId())
//                .toUri();
        return new ResponseEntity<>(materialRequest.getId(), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Resource> updateStatus(@RequestBody RequestStatus status,
                                                 @PathVariable("id") Long id) {
        User user = AuthUtils.getCurrentUser();
        materialRequestService.updateStatus(id, status, user);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<Page<MaterialRequestResponseShort>> search(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "80", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "ref", required = false) Long ref,
            @RequestParam(value = "store", required = false) Long store,
            @RequestParam(value = "user", required = false) Long user,
            @RequestParam(value = "phase", required = false) String phase,
            @RequestParam(value = "status", required = false) String status
    ) {
        Page<MaterialRequestResponseShort> response = materialRequestService.search( page,  size, sort, search, ref, store, user, phase, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialRequestResponse> getOne(@PathVariable("id") Long id){
        return ResponseEntity.ok(materialRequestService.getOne(id));
    }




}
