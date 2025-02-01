package com.nipponest.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nipponest.models.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

}
