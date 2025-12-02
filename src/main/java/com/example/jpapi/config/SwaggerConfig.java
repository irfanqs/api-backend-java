package com.example.jpapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Backend - POS System")
                .version("1.0.0")
                .description("Backend API untuk sistem POS dengan role-based access (Kasir & Admin)")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@example.com")));
    }
}
