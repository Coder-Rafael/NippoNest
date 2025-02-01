package com.nipponest.DTOs;

public record ProductRegDTO(
    String nome,
    String descricao, 
    Double preco,
    int estoque,
    String genero,
    String tipoProduto
) {
}
