package com.nipponest.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nipponest.DTOs.HomeUserDTO;
import com.nipponest.DTOs.ProductRegDTO;
import com.nipponest.DTOs.ProductResponseDTO;
import com.nipponest.models.ProductModel;
import com.nipponest.models.UserModel;
import com.nipponest.repositories.ProductRepository;
import com.nipponest.repositories.UserRepository;
import com.nipponest.security.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    public ProductResponseDTO createProduct(ProductRegDTO productDTO, List<MultipartFile> files, UUID userId) {
        // Busca o usuário
        UserModel user = userRepository.findById(userId)
            .orElseThrow();

        // Cria o produto
        ProductModel product = new ProductModel();
        product.setNome(productDTO.nome());
        product.setDescricao(productDTO.descricao());
        product.setPreco(productDTO.preco());
        product.setEstoque(productDTO.estoque());
        product.setGenero(productDTO.genero());
        product.setTipo_produto(productDTO.tipoProduto());
        product.setUser(user);

        // Salva o produto antes de processar as imagens
        product = productRepository.save(product);

        // Salva as imagens e associa ao produto
        List<String> imagePaths = fileStorageService.storeProductImages(files, product.getId());
        product.setImagem(imagePaths);

        // Atualiza o produto com as URLs das imagens
        productRepository.save(product);

        // Converte para DTO de resposta
        return convertToResponseDTO(product);
    }

    private ProductResponseDTO convertToResponseDTO(ProductModel product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getNome(),
            product.getDescricao(),
            product.getPreco(),
            product.getImagem(),
            new HomeUserDTO(product.getUser().getId(), product.getUser().getName(), product.getUser().getPhone()) 
        );
    }

    private String buildFileUrl(String path, String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path)
        .path(fileName)
        .toUriString();
    }
}

