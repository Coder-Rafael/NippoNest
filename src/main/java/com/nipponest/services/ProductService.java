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

    //Cria um produto
    public ProductResponseDTO createProduct(ProductRegDTO productDTO, List<MultipartFile> files, UUID userId) {
        // Busca o usuário
        UserModel user = userRepository.findById(userId)
            .orElseThrow();

        // Cria o produtoModel
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

        productRepository.save(product);

        return convertToResponseDTO(product);
    }

    public ProductModel updateProductImages(UUID productId, List<MultipartFile> files) {

        ProductModel product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Exclui imagens antigas (se necessário)
        List<String> oldImages = product.getImagem();
        if (oldImages != null && !oldImages.isEmpty()) {
            fileStorageService.deleteFiles(oldImages);
        }

        // Armazena as novas imagens e atualiza o produto
        List<String> newImagePaths = fileStorageService.storeProductImages(files, productId);
        product.setImagem(newImagePaths);

        return productRepository.save(product);
    }

     // Atualizar uma imagem específica
     public ProductModel updateSpecificImage(UUID productId, int imageIndex, MultipartFile file) {
        ProductModel product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Valida o índice da imagem
        if (imageIndex < 0 || imageIndex >= product.getImagem().size()) {
            throw new IllegalArgumentException("Índice de imagem inválido");
        }

        String oldImage = product.getImagem().get(imageIndex);
        fileStorageService.deleteFile(oldImage);

        String newImagePath = fileStorageService.updateProductImage(file, productId);

        product.getImagem().set(imageIndex, newImagePath);

        return productRepository.save(product);
    }

    // Adicionar novas imagens ao produto
    public ProductModel addNewImages(UUID productId, List<MultipartFile> files) {
        ProductModel product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        // Armazena as novas imagens
        List<String> newImagePaths = fileStorageService.storeProductImages(files, productId);

        // Adiciona as novas imagens à lista existente
        product.getImagem().addAll(newImagePaths);

        return productRepository.save(product);
    }

    public void deleteProduct(UUID productId) {
        ProductModel product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        List<String> imagePaths = product.getImagem();
        if (imagePaths != null && !imagePaths.isEmpty()) {
            fileStorageService.deleteFiles(imagePaths);
        }
        productRepository.delete(product);
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

    // Valida um único arquivo
    public void validateImageFile(MultipartFile file) {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo inválido: " + file.getOriginalFilename());
        }
    }

    // Valida uma lista de arquivos
    public void validateImageFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            validateImageFile(file);
        }
    }

    private String buildFileUrl(String path, String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(path)
        .path(fileName)
        .toUriString();
    }
}

