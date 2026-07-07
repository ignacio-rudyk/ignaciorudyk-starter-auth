package com.ignaciorudyk.auth.service;

import com.ignaciorudyk.auth.repository.dto.request.LoginRequestDTO;
import com.ignaciorudyk.auth.repository.dto.request.RefreshTokenRequestDTO;
import com.ignaciorudyk.auth.repository.dto.request.RegisterRequestDTO;
import com.ignaciorudyk.auth.repository.dto.response.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);

    AuthResponseDTO refresh(RefreshTokenRequestDTO request);

    void logout(String refreshToken);

}