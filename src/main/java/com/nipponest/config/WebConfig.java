package com.nipponest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura a URL "/uploads/**" para buscar arquivos no diretório "uploads/"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");  // Caminho para a pasta de uploads
    }
}
