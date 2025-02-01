package com.nipponest.DTOs;

import java.util.UUID;

public record HomeDTO(
    UUID id, 
    String nome, 
    Double preco, 
    String imagem, 
    HomeUserDTO usuario) {
    
}
