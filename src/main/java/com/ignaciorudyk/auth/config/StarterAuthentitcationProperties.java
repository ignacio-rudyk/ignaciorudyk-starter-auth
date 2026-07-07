package com.ignaciorudyk.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "ignaciorudyk.authentication")
@Data
public class StarterAuthentitcationProperties {

    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

    private List<String> publicEndpoints = new ArrayList<>();
    private List<String> userEndpoints = new ArrayList<>();
    private List<String> adminEndpoints = new ArrayList<>();

    private List<String> allowedOrigins = new ArrayList<>();
    private List<String> allowedMethods = new ArrayList<>();
    private List<String> allowedHeaders = new ArrayList<>();
    private List<String> exposedHeaders = new ArrayList<>();

}