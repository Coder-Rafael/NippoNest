package com.nipponest.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nipponest.DTOs.HomeDTO;
import com.nipponest.services.HomeService;
import com.nipponest.services.ProductService;

@RestController
@RequestMapping("/home")
public class HomeController {
    
    @Autowired
    HomeService service;

    @Autowired
    ProductService productService;

    @GetMapping
    public List<HomeDTO> listHome() {
        return service.listLast();
    }
    //http://localhost:8080/search/{name}
    @GetMapping("/search/{name}")
    public ResponseEntity<Page<HomeDTO>> getProductsDTOBySearch(@PathVariable String name, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProductsPageable(name, pageable));
    }

    /*@GetMapping("/{tipo}")
    public ResponseEntity<List<HomeDTO>> listPerSection(@PathVariable String tipo) {
        return ResponseEntity.ok(service.listItensPerSection(tipo));
    }*/
}
