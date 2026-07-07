package com.ignaciorudyk.auth.config.security;

import com.ignaciorudyk.auth.autoconfigure.AuthAutoConfiguration;
import com.ignaciorudyk.auth.exception.handler.JwtAccessDeniedHandler;
import com.ignaciorudyk.auth.exception.handler.JwtAuthEntryPointHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@AutoConfiguration(after = AuthAutoConfiguration.class)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthEntryPointHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

}