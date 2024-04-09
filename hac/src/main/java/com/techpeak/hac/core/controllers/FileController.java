package com.techpeak.hac.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techpeak.hac.core.services.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{path}/{filename}")
    public byte[] getMethodName(@PathVariable("path") String path, @PathVariable("filename") String filename)
            throws Exception {
        System.out.println("path = " + path + " filename = " + filename);
        return fileService.getFile(path + "/" + filename);
    }

    @PostMapping("/images")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file,
            @RequestParam(defaultValue = "images", required = false) String path) throws Exception {
        String fileName = fileService.uploadFile(file, path);
        return new ResponseEntity<>(fileName, HttpStatus.CREATED);
    }

}
