package com.techpeak.hac.core.services.impl;

import com.techpeak.hac.core.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final Path root = Paths.get("uploads");

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) throw new AssertionError();
            int beginIndex = originalFilename.lastIndexOf(".");
            String newFilename = "";
            newFilename = UUID.randomUUID()
                    + originalFilename.substring(beginIndex);

            Files.copy(file.getInputStream(), this.root.resolve(newFilename));
            return newFilename;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public String UploadImage(MultipartFile file) throws Exception {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("This file is not an image.");
        }

        return uploadFile(file);
    }

    @Override
    public void deleteFile(String path) throws Exception {
        Path fileToDeletePath = Paths.get(path);
        Files.deleteIfExists(fileToDeletePath);
    }

    @Override
    public byte[] getFile(String path) throws Exception {
        Path filePath = Paths.get(root.toString(), path);
        return Files.readAllBytes(filePath);
    }

    @Override
    public String uploadFile(MultipartFile file, String path) throws Exception {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
            if (!Files.exists(Paths.get(root.toString(), path))) {
                Files.createDirectory(Paths.get(root.toString(), path));
            }

            String originalFilename = file.getOriginalFilename();
            String newFilename = path + "/" + UUID.randomUUID().toString()
                    + originalFilename.substring(originalFilename.lastIndexOf("."));

            Files.copy(file.getInputStream(), this.root.resolve(newFilename));
            return newFilename;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public String UploadImage(MultipartFile file, String path) throws Exception {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("This file is not an image.");
        }

        return uploadFile(file, path);
    }

}
