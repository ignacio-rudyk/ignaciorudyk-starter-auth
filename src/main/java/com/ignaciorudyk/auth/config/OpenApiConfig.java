package com.ignaciorudyk.auth.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Incluí el access token obtenido en /auth/login"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Security Login API")
                        .description("JWT Authentication con Spring Security 6 — Portfolio Project")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Portfolio")
                                .url("https://www.linkedin.com/in/ignacio-nahuel-rudyk-466936196/")));
    }
}
