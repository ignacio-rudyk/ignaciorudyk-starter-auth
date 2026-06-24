package com.ignaciorudyk.auth.controller;

import com.ignaciorudyk.auth.repository.dto.AuthResponseDTO;
import com.ignaciorudyk.auth.repository.dto.LoginRequestDTO;
import com.ignaciorudyk.auth.repository.dto.RefreshTokenRequestDTO;
import com.ignaciorudyk.auth.repository.dto.RegisterRequestDTO;
import com.ignaciorudyk.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Registro, login, refresh y logout")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo usuario")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login con email y contraseña")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Rotar refresh token y obtener nuevo access token")
    public AuthResponseDTO refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Revocar el refresh token activo (logout real)")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDTO request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

}