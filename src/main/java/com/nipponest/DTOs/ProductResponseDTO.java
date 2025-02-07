package com.nipponest.DTOs;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(
    UUID id,
    String nome,
    String descricao,
    Double preco,
    List<String> imageUrls,
    HomeUserDTO userDTO
) {}
