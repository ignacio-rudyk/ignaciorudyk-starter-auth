package com.ignaciorudyk.auth.repository.dto;

public record UserInfoDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String role
) {}