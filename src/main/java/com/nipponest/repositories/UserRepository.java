package com.nipponest.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.nipponest.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserDetails findByLogin(String login);
    //Optional<UserModel> findByLogin(String login);
    UserModel save(UserModel userModel);
}
