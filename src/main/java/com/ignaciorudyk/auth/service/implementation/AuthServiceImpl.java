package com.ignaciorudyk.auth.service.implementation;

import com.ignaciorudyk.auth.exception.EmailAlreadyExistsException;
import com.ignaciorudyk.auth.exception.InvalidTokenException;
import com.ignaciorudyk.auth.repository.RefreshTokenRepository;
import com.ignaciorudyk.auth.repository.UserRepository;
import com.ignaciorudyk.auth.repository.dto.*;
import com.ignaciorudyk.auth.repository.model.RefreshToken;
import com.ignaciorudyk.auth.repository.model.Role;
import com.ignaciorudyk.auth.repository.model.User;
import com.ignaciorudyk.auth.service.AuthService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtServiceImpl jwtServiceImpl;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final long refreshTokenExpiration;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           JwtServiceImpl jwtServiceImpl,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtServiceImpl = jwtServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        LOGGER.info("Nuevo usuario registrado: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidTokenException("Usuario no encontrado"));
        refreshTokenRepository.revokeAllUserTokens(user);
        LOGGER.info("Login exitoso: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public AuthResponseDTO refresh(RefreshTokenRequestDTO request) {
        RefreshToken storedToken = refreshTokenRepository
                .findByToken(request.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("Refresh token no encontrado"));
        if (!storedToken.isValid()) {
            // Si el token fue robado y alguien intentó usarlo después de revocar, limpiar todo
            refreshTokenRepository.revokeAllUserTokens(storedToken.getUser());
            throw new InvalidTokenException("Refresh token inválido o expirado. Iniciá sesión nuevamente.");
        }
        // Rotación: revocar el token viejo
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
        User user = storedToken.getUser();
        LOGGER.info("Refresh token rotado para: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    LOGGER.info("Logout: token revocado para usuario {}", token.getUser().getEmail());
                });
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        String accessToken = jwtServiceImpl.generateAccessToken(user);
        String refreshToken = createRefreshToken(user);
        UserInfoDTO userInfo = new UserInfoDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
        return AuthResponseDTO.of(accessToken, refreshToken, jwtServiceImpl.getAccessTokenExpiration(), userInfo);
    }

    private String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

}
