package com.ignaciorudyk.auth.config;

import com.ignaciorudyk.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (existsBearer(request, response, filterChain, authHeader)) return;
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        if (Objects.nonNull(userEmail) && securityContextAuthenticationIsNull()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails))
                setAuthenticationTokenInSecurityContext(userEmail, request, userDetails);
        }
        filterChain.doFilter(request, response);
    }

    private static void setAuthenticationTokenInSecurityContext(String userEmail, HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Usuario autenticado via JWT: {}", userEmail);
    }

    private static boolean existsBearer(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String authHeader) throws IOException, ServletException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Si no hay header o no empieza con "Bearer", pasar al siguiente filtro
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private static boolean Email(String userEmail) {
        return userEmail != null;
    }

    private static boolean securityContextAuthenticationIsNull() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

}
