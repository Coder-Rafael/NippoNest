package com.nipponest.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nipponest.DTOs.HomeUserDTO;
import com.nipponest.DTOs.ProductRegDTO;
import com.nipponest.DTOs.ProductResponseDTO;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
 
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private String nome;
    private String descricao;
    private Double preco;
    private int estoque;
    private String genero;
    //Atualizar para um ENUM com os tipos pre definidos, tipo Manga, Novel e etc :v
    private String tipo_produto;
    @ElementCollection
    private List<String> imagem;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    @JsonBackReference 
    private UserModel user;

    //Construtor para ProductRegDTO
    public ProductModel(ProductRegDTO produtoDTO) {
        this.nome = produtoDTO.nome();
        this.descricao = produtoDTO.descricao();
        this.preco = produtoDTO.preco();
        this.estoque = produtoDTO.estoque();
        this.genero = produtoDTO.genero();
        this.tipo_produto = produtoDTO.tipoProduto();
    }

    public ProductModel(ProductRegDTO produtoDTO, UserModel user) {
        this.nome = produtoDTO.nome();
        this.descricao = produtoDTO.descricao();
        this.preco = produtoDTO.preco();
        this.estoque = produtoDTO.estoque();
        this.genero = produtoDTO.genero();
        this.tipo_produto = produtoDTO.tipoProduto();
        this.user = user;
    }
}
