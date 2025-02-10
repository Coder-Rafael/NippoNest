package com.nipponest.DTOs;

import org.springframework.web.multipart.MultipartFile;

import com.nipponest.ENUMs.Genero;
import com.nipponest.ENUMs.TipoProduto;

import java.util.List;

public record ProductRegDTO(
    String nome,
    String descricao, 
    Double preco,
    int estoque,
    List<Genero> genero,
    TipoProduto tipoProduto
) {}

