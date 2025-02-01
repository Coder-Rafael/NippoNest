package com.nipponest.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nipponest.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {  

    System.out.println("🔍 [SecurityFilter] - Iniciando filtro de autenticação...");

    var token = this.recoverToken(request);
    System.out.println("📌 [SecurityFilter] - Token recuperado: " + token);

    if (token != null) {
        var email = tokenService.validateToken(token);
        System.out.println("📌 [SecurityFilter] - E-mail extraído do token: " + email);

        UserDetails user = userRepository.findByLogin(email);
        System.out.println("📌 [SecurityFilter] - Usuário encontrado: " + (user != null ? user.getUsername() : "NENHUM USUÁRIO ENCONTRADO!"));

        if (user != null) {
            var authorization = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authorization);
            System.out.println("✅ [SecurityFilter] - Autenticação definida no contexto.");
        } else {
            System.out.println("❌ [SecurityFilter] - Usuário não encontrado, não será autenticado.");
        }
    } else {
        System.out.println("⚠️ [SecurityFilter] - Nenhum token encontrado, seguindo sem autenticação.");
    }
    
    filterChain.doFilter(request, response);
}

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
    
}
