package com.nipponest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nipponest.DTOs.UserRegDTO;
import com.nipponest.models.UserModel;
import com.nipponest.repositories.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    public void save(UserRegDTO userRegDTO) {
        // Converter UserDTO para UserModel
        UserModel userModel = new UserModel();
        userModel.setName(userRegDTO.name());
        userModel.setLogin(userRegDTO.login());
        userModel.setSenha(userRegDTO.password());
        userModel = repository.save(userModel);
    }

     @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findByLogin(login);
            //.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
