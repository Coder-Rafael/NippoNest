package com.nipponest.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nipponest.DTOs.HomeDTO;
import com.nipponest.DTOs.HomeUserDTO;
import com.nipponest.models.ProductModel;
import com.nipponest.models.UserModel;
import com.nipponest.repositories.ProductRepository;

@Service
public class HomeService {
    @Autowired
    ProductRepository repository;

    @Autowired
    ProductService service;

    public List<HomeDTO> listLast() { 
    List<ProductModel> lastProducts = repository.findAllOrderedByIdDesc();
    List<HomeDTO> listResponse = new ArrayList<>();

    for (ProductModel productModel : lastProducts) {
        UserModel user = productModel.getUser();
        List<String> imageUrls = productModel.getImagem().stream()
        .map(imageName -> {
            // Verifica se o imageName já contém a URL completa (caso não precise do domínio novamente)
            if (imageName.startsWith("http")) {
                return imageName; // Retorna a URL completa já salva no banco
            } else {
                return "https://nipponest-production.up.railway.app/uploads/products/" + imageName; // Concatena o caminho correto
            }
        })
        .collect(Collectors.toList());

        HomeUserDTO userDTO = new HomeUserDTO(user.getId(), user.getName(), user.getPhone());

        listResponse.add(new HomeDTO(
            productModel.getId(), 
            productModel.getNome(), 
            productModel.getDescricao(), 
            productModel.getPreco(), 
            imageUrls,
            userDTO
        ));
    }

    return listResponse;
}
}
