package com.nipponest.DTOs;

import java.util.UUID;

public record ProductRegDTO(
    String nome,
    String descricao, 
    Double preco,
    int estoque,
    String genero,
    String tipoProduto
) {
}
