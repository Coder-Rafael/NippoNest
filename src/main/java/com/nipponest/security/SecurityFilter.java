package com.nipponest.security;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nipponest.models.UserModel;
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

    var token = this.recoverToken(request);

    if (token != null) {
        try {
            UUID userId = tokenService.getUserIdFromToken(token);  

            Optional<UserModel> optionalUser = userRepository.findById(userId); 

            if (optionalUser.isPresent()) {
                UserModel user = optionalUser.get(); // Obtém o usuário de dentro do Optional
                
                // Se o UserModel implementa UserDetails, você pode fazer o cast
                UserDetails userDetails = (UserDetails) user; // Ou use um adaptador caso não seja compatível diretamente
                
                var authorization = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authorization);
                logger.debug("Usuário autenticado: " + user.getUsername()); // Aqui você chama getUsername() no UserModel
            } else {
                logger.warn("Usuário não encontrado com o ID: " + userId);
            }
        } catch (JWTVerificationException e) {
            logger.warn("Token inválido ou expirado: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
            return; 
        }
    }

        filterChain.doFilter(request, response);
    } 

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
    
}
