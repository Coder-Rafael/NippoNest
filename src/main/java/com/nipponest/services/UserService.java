package com.nipponest.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nipponest.models.UserModel;
import com.nipponest.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    
    public UserModel updateUserAvatar(UUID userId, MultipartFile file) {
        UserModel user = userRepository.findById(userId)
        .orElseThrow();

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Apenas imagens são permitidas");
        }
        
        String fileName = fileStorageService.storeUserAvatar(file, userId);
        String fileUrl = buildFileUrl("/api/files/users/", fileName);
        user.setImgAvatar(fileUrl);
        return userRepository.save(user);
    }
    
    private String buildFileUrl(String path, String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path)
        .path(fileName)
        .toUriString();
    }
}
