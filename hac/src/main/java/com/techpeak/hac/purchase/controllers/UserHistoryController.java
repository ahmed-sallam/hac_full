package com.techpeak.hac.purchase.controllers;

import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.core.services.UserHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-histories")
@RequiredArgsConstructor
@Tag(name = "User Histories")
public class UserHistoryController {
    private final UserHistoryService userHistoryService;

    @GetMapping
    public ResponseEntity<List<UserHistoryResponse>> getItemHistory(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "tableName") String tableName) {
        return ResponseEntity.ok(userHistoryService.getItemHistory(tableName, id));
    }
}
