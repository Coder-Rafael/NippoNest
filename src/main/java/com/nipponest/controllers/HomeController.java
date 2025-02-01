package com.nipponest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nipponest.DTOs.HomeDTO;
import com.nipponest.services.HomeService;

@RestController
@RequestMapping("/home")
public class HomeController {
    
    @Autowired
    HomeService service;

    @GetMapping
    public List<HomeDTO> listHome() {
        return service.listLast();
    }
}
