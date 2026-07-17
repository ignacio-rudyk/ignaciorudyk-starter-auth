package com.ignaciorudyk.auth.config;

import java.util.ArrayList;
import java.util.List;

public record CustomRole(
        String roleName,
        List<String> endpoints
) {

    public CustomRole {
        endpoints = new ArrayList<>(endpoints);
    }

}