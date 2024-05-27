package com.techpeak.hac.inventory.controllers;

import com.techpeak.hac.inventory.dtos.CreateMachinePart;
import com.techpeak.hac.inventory.dtos.MachinePartResponse;
import com.techpeak.hac.inventory.dtos.UpdateMachinePart;
import com.techpeak.hac.inventory.services.MachinePartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/machine-parts")
@RequiredArgsConstructor
public class MachinePartController {
    private final MachinePartService machinePartService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateMachinePart createMachinePart) {
        machinePartService.create(createMachinePart);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UpdateMachinePart updateMachinePart) {
        machinePartService.update(id, updateMachinePart);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<MachinePartResponse>> list(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "80") Integer size,
                                                          @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive, @RequestParam(value = "name", defaultValue = "") String name
    ) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<MachinePartResponse> data = machinePartService.list(pageRequest, isActive, name);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachinePartResponse> get(@PathVariable("id") Long id) {
        MachinePartResponse data = machinePartService.get(id);
        return ResponseEntity.ok(data);
    }

}
