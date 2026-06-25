package com.ignaciorudyk.auth.controller;

import com.ignaciorudyk.auth.repository.dto.LoginRequestDTO;
import com.ignaciorudyk.auth.repository.dto.RefreshTokenRequestDTO;
import com.ignaciorudyk.auth.repository.dto.RegisterRequestDTO;
import com.ignaciorudyk.auth.repository.dto.response.ResponseDTO;
import com.ignaciorudyk.auth.service.AuthService;
import com.ignaciorudyk.auth.util.HttpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Registro, login, refresh y logout")
public class AuthController {

    private final AuthService authService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<ResponseDTO> register(HttpServletRequest httpServletRequest, @Valid @RequestBody RegisterRequestDTO request) {
        LOGGER.info("Llamado al servicio /register - User: {}", request.email());
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login con email y contraseña")
    public ResponseEntity<ResponseDTO> login(HttpServletRequest httpServletRequest, @Valid @RequestBody LoginRequestDTO request) {
        LOGGER.info("Llamado al servicio /login - User: {}", request.email());
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Rotar refresh token y obtener nuevo access token")
    public ResponseEntity<ResponseDTO> refresh(HttpServletRequest httpServletRequest, @Valid @RequestBody RefreshTokenRequestDTO request) {
        LOGGER.info("Llamado al servicio /refresh");
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, authService.refresh(request));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Revocar el refresh token activo (logout real)")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest httpServletRequest, @Valid @RequestBody RefreshTokenRequestDTO request) {
        LOGGER.info("Llamado al servicio /logout");
        authService.logout(request.refreshToken());
        return HttpUtil.isSucceful2xxResponse(httpServletRequest, null);
    }

}