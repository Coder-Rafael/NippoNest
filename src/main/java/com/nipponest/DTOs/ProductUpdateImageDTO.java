package com.nipponest.DTOs;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record ProductUpdateImageDTO(UUID productId, List<MultipartFile> images) {
    
}
