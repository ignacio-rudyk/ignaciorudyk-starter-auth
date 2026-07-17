package com.ignaciorudyk.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

@ConfigurationProperties(prefix = "ignaciorudyk.authentication")
public record StarterAuthenticationProperties(
        String secretKey,
        long accessTokenExpiration,
        long refreshTokenExpiration,
        List<CustomRole> customRoles,
        List<String> allowedOrigins,
        List<String> allowedMethods,
        List<String> allowedHeaders,
        List<String> exposedHeaders
) {

    public void addCustomRole(CustomRole newCustomRole) {
        Optional<CustomRole> existingRole = customRoles.stream()
                .filter(role -> role.roleName().equalsIgnoreCase(newCustomRole.roleName()))
                .findFirst();
        if (existingRole.isPresent())
            existingRole.get().endpoints().addAll(newCustomRole.endpoints());
        else
            customRoles.add(newCustomRole);
    }

}