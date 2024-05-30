package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.utils.AuthUtils;
import com.techpeak.hac.purchase.dtos.RFPQResponse;
import com.techpeak.hac.purchase.dtos.RFPQResponseShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.services.RFPQService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/rfpqs")
@RequiredArgsConstructor
@Validated
public class RFPQController {
    private final RFPQService rfpqService;


    @GetMapping
    public ResponseEntity<Page<RFPQResponseShort>> search(
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
        Page<RFPQResponseShort> response = rfpqService.search( page,  size, sort, search, ref, store, user, phase, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RFPQResponse> getOne(@PathVariable("id") Long id){
        return ResponseEntity.ok(rfpqService.getOne(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Resource> updateStatus(@RequestBody RequestStatus status,
                                                 @PathVariable("id") Long id) {

        User user = AuthUtils.getCurrentUser();
        rfpqService.updateStatus(id, status, user);
        return ResponseEntity.ok().build();
    }
}
