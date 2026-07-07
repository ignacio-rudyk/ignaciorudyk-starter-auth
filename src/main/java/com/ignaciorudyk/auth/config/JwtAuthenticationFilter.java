package com.ignaciorudyk.auth.config;

import com.ignaciorudyk.auth.repository.dto.UserInfoDTO;
import com.ignaciorudyk.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (existsBearer(request, response, filterChain, authHeader)) return;
        final String jwt = authHeader.substring(7);
        if (!jwtService.isTokenValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (securityContextAuthenticationIsNull())
            buildAuthenticationToken(jwt, request);
        filterChain.doFilter(request, response);
    }

    private static boolean existsBearer(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String authHeader) throws IOException, ServletException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private static boolean securityContextAuthenticationIsNull() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void buildAuthenticationToken(String jwt, HttpServletRequest request) {
        UserInfoDTO claims = jwtService.extractClaims(jwt);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        claims,
                        null,
                        List.of(new SimpleGrantedAuthority(claims.role()))
                );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Usuario autenticado via JWT: userId={}", claims.id());
    }

}