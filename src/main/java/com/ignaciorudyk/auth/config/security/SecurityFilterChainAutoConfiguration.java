package com.ignaciorudyk.auth.config.security;

import com.ignaciorudyk.auth.config.JwtAuthenticationFilter;
import com.ignaciorudyk.auth.config.StarterAuthentitcationProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@AutoConfiguration(after = SecurityAutoConfiguration.class)
public class SecurityFilterChainAutoConfiguration {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final StarterAuthentitcationProperties starterAuthentitcationProperties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] INTERNAL_PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/error",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String[] INTERNAL_USER_ENDPOINTS = {"/users/me"};

    private static final String[] INTERNAL_ADMIN_ENDPOINTS = {"/users/admin/**"};

    public SecurityFilterChainAutoConfiguration(AuthenticationEntryPoint authenticationEntryPoint,
                                     AccessDeniedHandler accessDeniedHandler,
                                     UserDetailsService userDetailsService,
                                     PasswordEncoder passwordEncoder,
                                     StarterAuthentitcationProperties starterAuthentitcationProperties, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.starterAuthentitcationProperties = starterAuthentitcationProperties;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectProvider<SecurityCustomizer> customizer) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        .requestMatchers(getUserEndpoints()).hasRole("USER")
                        .requestMatchers(getAdminEndpoints()).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        setCustomizer(http, customizer);
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(starterAuthentitcationProperties.getAllowedOrigins());
        config.setAllowedMethods(starterAuthentitcationProperties.getAllowedMethods());
        config.setAllowedHeaders(starterAuthentitcationProperties.getAllowedHeaders());
        config.setExposedHeaders(starterAuthentitcationProperties.getExposedHeaders());
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private String[] getPublicEndpoints() {
        starterAuthentitcationProperties.getPublicEndpoints().addAll(List.of(INTERNAL_PUBLIC_ENDPOINTS));
        return starterAuthentitcationProperties.getPublicEndpoints().toArray(String[]::new);
    }

    private String[] getUserEndpoints() {
        starterAuthentitcationProperties.getUserEndpoints().addAll(List.of(INTERNAL_USER_ENDPOINTS));
        return starterAuthentitcationProperties.getUserEndpoints().toArray(String[]::new);
    }

    private String[] getAdminEndpoints() {
        starterAuthentitcationProperties.getAdminEndpoints().addAll(List.of(INTERNAL_ADMIN_ENDPOINTS));
        return starterAuthentitcationProperties.getAdminEndpoints().toArray(String[]::new);
    }


    private static void setCustomizer(HttpSecurity http, ObjectProvider<SecurityCustomizer> customizer) {
        customizer.ifAvailable(c -> {
            try { c.customize(http); }
            catch (Exception e) { throw new RuntimeException(e); }
        });
    }

}