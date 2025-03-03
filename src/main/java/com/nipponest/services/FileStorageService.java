package com.nipponest.services;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths; 
import java.nio.file.Files;
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

    @Value("${app.base-url}")
    private String baseUrl;

    public FileStorageService(
        @Value("${file.upload-dir-users}") String userDir,
        @Value("${file.upload-dir-products}") String productDir) {
        this.userUploadDir = Paths.get(userDir).toAbsolutePath().normalize();
        this.productUploadDir = Paths.get(productDir).toAbsolutePath().normalize();
        System.out.println("Base URL: " + this.baseUrl);
    }

    //METODOS PRA USUARIO
    public String storeUserAvatar(MultipartFile file, UUID userId) {
        String fileName = "user-" + userId + getFileExtension(file);
        String savedFileName = storeFile(file, userUploadDir, fileName);
        return generateFileUrl("users", savedFileName); // Gera a URL completa
    }

    // Métodos para produtos
    public List<String> storeProductImages(List<MultipartFile> files, UUID productId) {
        List<String> fileNames = new ArrayList<>();
        int index = 1;
        for (MultipartFile file : files) {
            String fileName = "product-" + productId + "-" + index + getFileExtension(file);
            String savedFileName = storeFile(file, productUploadDir, fileName);
            fileNames.add(generateFileUrl("products", savedFileName)); // Gera a URL completa
            index++;
        }
        return fileNames;
    }

    public String updateProductImage(MultipartFile file, UUID productId) {
        String fileName = "product-" + productId + "-" + UUID.randomUUID() + getFileExtension(file);
        String savedFileName = storeFile(file, productUploadDir, fileName);
        return generateFileUrl("products", savedFileName); // Gera a URL completa
    }

    //METODOS DO SERVICE
    private String storeFile(MultipartFile file, Path targetDir, String fileName) {
        try {
            // Garante que o fileName seja apenas o nome do arquivo
            String simpleFileName = Paths.get(fileName).getFileName().toString();
            System.out.println("Nome do arquivo:"+fileName);
            System.out.println("Nome simples do arquivo:"+simpleFileName);

            Path targetPath = targetDir.resolve(simpleFileName);
            file.transferTo(targetPath);
            return simpleFileName; // Retorna apenas o nome do arquivo
        } catch (IOException ex) {
            throw new RuntimeException("Falha ao salvar arquivo", ex);
        }
    }

    // Excluir uma única imagem
    public void deleteFile(String fileName) {
        try {
            Path filePath = productUploadDir.resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Falha ao excluir arquivo: " + fileName, ex);
        }
    }

    public void deleteFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            deleteFile(filePath);
        }
    }

    // Gera a URL completa do arquivo
    private String generateFileUrl(String type, String fileName) {
        return baseUrl + "/uploads/" + type + "/" + fileName;
    }

    private String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return "";
    }
}
