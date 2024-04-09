package com.techpeak.hac.core.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file) throws Exception;

    String uploadFile(MultipartFile file, String path) throws Exception;

    String UploadImage(MultipartFile file) throws Exception;

    String UploadImage(MultipartFile file, String path) throws Exception;

    void deleteFile(String path) throws Exception;

    byte[] getFile(String path) throws Exception;
}
