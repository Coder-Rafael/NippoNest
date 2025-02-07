package com.nipponest.services;

import java.io.IOException;
import java.nio.file.Path; // ✅ Import correto para Path
import java.nio.file.Paths; // ✅ Import correto para Paths
import java.nio.file.Files; // ✅ Import correto para Files
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path userUploadDir;
    private final Path productUploadDir;

    public FileStorageService(
        @Value("${file.upload-dir-users}") String userDir,
        @Value("${file.upload-dir-products}") String productDir) {
        this.userUploadDir = Paths.get(userDir).toAbsolutePath().normalize();
        this.productUploadDir = Paths.get(productDir).toAbsolutePath().normalize();
    }

    public String storeUserAvatar(MultipartFile file, UUID userId) {
        String fileName = "user-" + userId + getFileExtension(file);
        return storeFile(file, userUploadDir, fileName);
    }   

    public List<String> storeProductImages(List<MultipartFile> files, UUID productId) {
        List<String> fileNames = new ArrayList<>();
        int index = 1;
        for (MultipartFile file : files) {
            String fileName = "product-" + productId + "-" + index + getFileExtension(file);
            storeFile(file, productUploadDir, fileName);
            fileNames.add(fileName);
            index++;
        }
        return fileNames;
    }
    private String storeFile(MultipartFile file, Path targetDir, String fileName) {
        try {
            Path targetPath = targetDir.resolve(fileName);
            file.transferTo(targetPath);
            return fileName;
        } catch (IOException ex) {
        throw new RuntimeException("Falha ao salvar arquivo", ex);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        return originalFileName != null ?
        originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
    }
}
