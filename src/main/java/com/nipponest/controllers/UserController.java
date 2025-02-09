package com.nipponest.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nipponest.models.UserModel;
import com.nipponest.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
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

     // Self-delete: Usuário deleta a própria conta
     @DeleteMapping("/self")
     public ResponseEntity<Void> deleteSelf(Authentication authentication) {
         UUID userId = ((UserModel) authentication.getPrincipal()).getId();
         userService.deleteUserById(userId);
         return ResponseEntity.noContent().build();
     }
 
     /* 
     // Delete por Admin: Admin deleta qualquer conta
     @DeleteMapping("/{userId}")
     public ResponseEntity<Void> deleteUserAsAdmin(@PathVariable UUID userId) {
         userService.deleteUserById(userId);
         return ResponseEntity.noContent().build();
     }*/
}
