package com.ignaciorudyk.auth.repository.dto.response;

import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UserInfoDTO user
) {
    public static AuthResponseDTO of(String accessToken, String refreshToken,
                                  Long expiresIn, UserInfoDTO user) {
        return new AuthResponseDTO(accessToken, refreshToken, "Bearer", expiresIn, user);
    }
}