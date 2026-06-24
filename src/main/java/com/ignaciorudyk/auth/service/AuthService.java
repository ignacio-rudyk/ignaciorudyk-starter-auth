package com.ignaciorudyk.auth.service;

import com.ignaciorudyk.auth.repository.dto.AuthResponseDTO;
import com.ignaciorudyk.auth.repository.dto.LoginRequestDTO;
import com.ignaciorudyk.auth.repository.dto.RefreshTokenRequestDTO;
import com.ignaciorudyk.auth.repository.dto.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);

    AuthResponseDTO refresh(RefreshTokenRequestDTO request);

    void logout(String refreshToken);

}
