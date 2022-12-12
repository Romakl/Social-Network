package com.romankliuiev.socialnetwork.web;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romankliuiev.socialnetwork.config.filter.CustomAuthenticationFilter;
import com.romankliuiev.socialnetwork.config.filter.CustomAuthorizationFilter;
import com.romankliuiev.socialnetwork.data.InactiveToken;
import com.romankliuiev.socialnetwork.data.user.User;
import com.romankliuiev.socialnetwork.service.InactiveTokenService;
import com.romankliuiev.socialnetwork.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthController {
    private final UserService userService;
    private final InactiveTokenService inactiveTokenService;

    public AuthController(UserService userService, InactiveTokenService inactiveTokenService) {
        this.userService = userService;
        this.inactiveTokenService = inactiveTokenService;
    }

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring(7);
                DecodedJWT decodedJWT = CustomAuthorizationFilter.verifyToken(refreshToken);

                if (inactiveTokenService.getInactiveTokenFromCache(refreshToken) != null) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }

                User user = userService.getUserByUsername(decodedJWT.getSubject());
                List<String> roles = user.getRoles().stream().map(role -> role.getName().name()).toList();

                String accessToken = CustomAuthenticationFilter.generateAccessToken(request, user.getUsername(), roles);
                refreshToken = CustomAuthenticationFilter.generateRefreshToken(request, user.getUsername(), roles);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (IOException e) {
                throw new RuntimeException("Can't write to response");
            } catch (SignatureVerificationException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            throw new BadCredentialsException("Refresh token is missing");
        }
    }

    @PostMapping("/logout")
    private ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken, @RequestHeader("Authorization-refresh") String refreshToken) {
        DecodedJWT decodedAccessToken = CustomAuthorizationFilter.verifyToken(accessToken.substring(7));
        DecodedJWT decodedRefreshToken = CustomAuthorizationFilter.verifyToken(refreshToken.substring(7));

        InactiveToken inactiveAccessToken = new InactiveToken();
        inactiveAccessToken.setToken(decodedAccessToken.getToken());
        inactiveAccessToken.setExpiration(OffsetDateTime.ofInstant(decodedAccessToken.getExpiresAtAsInstant(), ZoneOffset.UTC));

        InactiveToken inactiveRefreshToken = new InactiveToken();
        inactiveRefreshToken.setToken(decodedRefreshToken.getToken());
        inactiveRefreshToken.setExpiration(OffsetDateTime.ofInstant(decodedRefreshToken.getExpiresAtAsInstant(), ZoneOffset.UTC));

        inactiveTokenService.createInactiveToken(inactiveAccessToken);
        inactiveTokenService.createInactiveToken(inactiveRefreshToken);

        return ResponseEntity.ok().build();
    }

}
