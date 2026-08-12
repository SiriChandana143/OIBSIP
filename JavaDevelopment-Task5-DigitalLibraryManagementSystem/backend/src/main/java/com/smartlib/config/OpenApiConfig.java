package com.smartlib.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartLibOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SMARTLIB AI API")
                        .description("Intelligent Digital Library Management System REST API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SMARTLIB AI Team")
                                .email("support@smartlib.ai")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
