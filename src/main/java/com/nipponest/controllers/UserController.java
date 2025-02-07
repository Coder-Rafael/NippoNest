package com.nipponest.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nipponest.models.UserModel;
import com.nipponest.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @PutMapping("/avatar")
    public ResponseEntity<UserModel> updateAvatar(
        @RequestParam("file") MultipartFile file,
        Authentication authentication) {
            
    UUID userId = ((UserModel) authentication.getPrincipal()).getId();
    UserModel updatedUser = userService.updateUserAvatar(userId, file);
    return ResponseEntity.ok(updatedUser);
    }
}
