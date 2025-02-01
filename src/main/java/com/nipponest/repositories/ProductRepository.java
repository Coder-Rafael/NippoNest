package com.nipponest.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nipponest.models.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
@Query("SELECT p FROM product p ORDER BY p.id DESC")
    List<ProductModel> findAllOrderedByIdDesc();
}
