package com.techpeak.hac.core.controllers;

import com.techpeak.hac.core.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{path}/{filename}")
    public byte[] getMethodName(@PathVariable("path") String path, @PathVariable("filename") String filename)
            throws Exception {
        return fileService.getFile(path + "/" + filename);
    }

    @PostMapping("/images")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file,
            @RequestParam(defaultValue = "images", required = false) String path) throws Exception {
        String fileName = fileService.uploadFile(file, path);
        return new ResponseEntity<>(fileName, HttpStatus.CREATED);
    }

}
