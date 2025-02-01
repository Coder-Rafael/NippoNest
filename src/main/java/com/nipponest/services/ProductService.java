package com.nipponest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.nipponest.DTOs.ProductRegDTO;
import com.nipponest.models.ProductModel;
import com.nipponest.models.UserModel;
import com.nipponest.repositories.ProductRepository;
import com.nipponest.repositories.UserRepository;
import com.nipponest.security.TokenService;

@Service
public class ProductService {
    
    @Autowired
    ProductRepository pRepository;
    @Autowired
    UserRepository uRepository;
    @Autowired
    TokenService service;
    
    public void saveProduct(ProductRegDTO produtoDTO, String Token) {
        UserModel user = getUserFromToken(Token);
        ProductModel produto = new ProductModel(produtoDTO);
        produto.setUser(user);
        pRepository.save(produto);
    }

    public UserModel getUserFromToken(String token) {
            String login = service.validateToken(token);

            if (!login.isEmpty()) {
                UserModel user = (UserModel) uRepository.findByLogin(login);
                return user;
            }

            throw new BadCredentialsException("Token inválido");
        }
}
