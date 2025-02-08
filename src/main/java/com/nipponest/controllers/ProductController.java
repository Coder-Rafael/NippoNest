package com.nipponest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nipponest.DTOs.ProductRegDTO;
import com.nipponest.DTOs.ProductResponseDTO;
import com.nipponest.models.ProductModel;
import com.nipponest.models.UserModel;
import com.nipponest.services.FileStorageService;
import com.nipponest.services.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService; 

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
        @RequestPart("product") @Valid ProductRegDTO productDTO, 
        @RequestPart("imagens") List<MultipartFile> files, 
        Authentication authentication) {

        UUID userId = ((UserModel) authentication.getPrincipal()).getId();
        ProductResponseDTO savedProduct = productService.createProduct(productDTO, files, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

   @PatchMapping("/{productId}/images")
    public ResponseEntity<ProductModel> updateProductImages(
        @PathVariable UUID productId,
        @RequestParam("files") List<MultipartFile> files) {
            productService.validateImageFiles(files);
            ProductModel updatedProduct = productService.updateProductImages(productId, files);
            return ResponseEntity.ok(updatedProduct);
    }

    // Atualizar uma imagem específica
    @PatchMapping("/{productId}/images/{imageIndex}")
    public ResponseEntity<ProductModel> updateSpecificProductImage(
        @PathVariable UUID productId,
        @PathVariable int imageIndex,
        @RequestParam("file") MultipartFile file) {
        
        productService.validateImageFile(file);
        
        ProductModel updatedProduct = productService.updateSpecificImage(productId, imageIndex, file);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
      productService.deleteProduct(productId);
      return ResponseEntity.noContent().build();
    }
}
