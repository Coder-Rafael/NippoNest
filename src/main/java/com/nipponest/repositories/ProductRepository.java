package com.nipponest.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nipponest.models.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    @Query("SELECT p FROM product p ORDER BY p.id DESC")
    List<ProductModel> findAllOrderedByIdDesc();

    @Query("SELECT p FROM product p WHERE p.nome LIKE :nome%")
    Page<ProductModel> findAllByNome(@Param("nome") String nome, Pageable pageable);

}
