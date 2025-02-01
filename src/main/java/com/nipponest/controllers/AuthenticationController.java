package com.nipponest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nipponest.DTOs.LoginDTO;
import com.nipponest.DTOs.LoginTokenDTO;
import com.nipponest.DTOs.UserRegDTO;
import com.nipponest.models.UserModel;
import com.nipponest.security.TokenService;
import com.nipponest.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService service;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    // localhost:8080/auth/login
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return (ResponseEntity) ResponseEntity.ok(new LoginTokenDTO(token));
    }

    // https://mangahub-production.up.railway.app/auth/register
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegDTO userDTOreg) {
        if (this.service.loadUserByUsername(userDTOreg.login()) != null)
        return ResponseEntity.badRequest().body("Usuário já existe!");
        //return ResponseEntity.badRequest().build();

        String encriptedPassowrd = new BCryptPasswordEncoder().encode(userDTOreg.password());
        UserRegDTO userDto = new UserRegDTO(userDTOreg.name(), userDTOreg.login(), encriptedPassowrd);
        service.save(userDto);
        return ResponseEntity.ok().build();
    }
}
