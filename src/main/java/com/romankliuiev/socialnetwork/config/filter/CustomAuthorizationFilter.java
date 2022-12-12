package com.romankliuiev.socialnetwork.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.romankliuiev.socialnetwork.service.InactiveTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final static String secret = System.getenv("JWT_SECRET");
    private final InactiveTokenService inactiveTokensService;

    public CustomAuthorizationFilter(InactiveTokenService inactiveTokensService) {
        this.inactiveTokensService = inactiveTokensService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/login") || request.getServletPath().equals("/api/v1/login/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring(7);

                    DecodedJWT decodedJWT;
                    try {
                        decodedJWT = verifyToken(token);
                    } catch (Exception e) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        return;
                    }

                    if (inactiveTokensService.getInactiveTokenFromCache(token) != null) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        return;
                    }

                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (IOException e) {
                    throw new RuntimeException("Can't write to response");
                } catch (SignatureVerificationException e) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public static DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());

        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }


    private static boolean checkIfAuthenticatedUserHasRoles(Authentication authentication, String... roles) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> Arrays.asList(roles).contains(authority));
    }
}
