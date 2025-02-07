package com.nipponest.DTOs;

import java.util.List;
import java.util.UUID;

public record HomeDTO(
    UUID id, 
    String nome, 
    Double preco, 
    List<String> imagem, 
    HomeUserDTO usuario) {
}
