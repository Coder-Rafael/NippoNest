package com.nipponest.DTOs;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductRegDTO(
    String nome,
    String descricao, 
    Double preco,
    int estoque,
    String genero,
    String tipoProduto
) {}

