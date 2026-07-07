package com.ignaciorudyk.auth.service.implementation;

import com.ignaciorudyk.auth.config.StarterAuthentitcationProperties;
import com.ignaciorudyk.auth.repository.RefreshTokenRepository;
import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;
import com.ignaciorudyk.auth.repository.model.RefreshToken;
import com.ignaciorudyk.auth.repository.model.User;
import com.ignaciorudyk.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final StarterAuthentitcationProperties starterAuthentitcationProperties;


    public JwtServiceImpl(RefreshTokenRepository refreshTokenRepository, StarterAuthentitcationProperties starterAuthentitcationProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.starterAuthentitcationProperties = starterAuthentitcationProperties;
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        User user = (User) userDetails;
        extraClaims.put("userId", user.getId());
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("role", userDetails.getAuthorities()
                .stream().findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER"));
        return buildToken(extraClaims, userDetails,
                starterAuthentitcationProperties.getAccessTokenExpiration());
    }

    @Override
    public String generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(starterAuthentitcationProperties.getRefreshTokenExpiration() / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public UserInfoDTO extractClaims(String token) {
        Claims claims = extractAllClaims(token);
        return new UserInfoDTO(
                claims.get("userId", Long.class),
                claims.getSubject(),
                claims.get("firstName", String.class),
                claims.get("lastName", String.class),
                claims.get("role", String.class)
        );
    }

    @Override
    public long getAccessTokenExpiration() {
        return starterAuthentitcationProperties.getAccessTokenExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(starterAuthentitcationProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

}