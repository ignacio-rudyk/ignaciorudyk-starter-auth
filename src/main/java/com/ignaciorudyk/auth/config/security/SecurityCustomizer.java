package com.ignaciorudyk.auth.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Callback de personalización del SecurityFilterChain.
 *
 * Implementá este bean en tu proyecto consumidor para agregar
 * configuración adicional de seguridad (endpoints públicos, reglas
 * de autorización, filtros propios) sin reemplazar el FilterChain
 * completo que provee el starter.
 *
 * Ejemplo:
 * <pre>
 * {@code
 * @Bean
 * public SecurityCustomizer mySecurityCustomizer() {
 *     return http -> http.authorizeHttpRequests(auth -> auth
 *             .requestMatchers("/productos/**").permitAll()
 *             .anyRequest().authenticated()
 *     );
 * }
 * }
 * </pre>
 */
@FunctionalInterface
public interface SecurityCustomizer {
    void customize(HttpSecurity http) throws Exception;
}