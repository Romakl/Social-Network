package com.romankliuiev.socialnetwork.config.filter;

import com.romankliuiev.socialnetwork.dto.user.UserLoginDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final static String JWT_SECRET = System.getenv("JWT_SECRET");
    private final static String JWT_EXPIRATION = System.getenv("JWT_EXPIRATION");

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserLoginDTO loginInfo;
        try {
            loginInfo = new ObjectMapper().readValue(request.getInputStream(), UserLoginDTO.class);
        } catch (IOException e) {
            throw new UsernameNotFoundException("User not found");
        }
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Authentication failed for user: " + username);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        String accessToken = generateAccessToken(request, user.getUsername(), roles);
        String refreshToken = generateRefreshToken(request, user.getUsername(), roles);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    public static String generateAccessToken(HttpServletRequest request, String username, List<String> roles) {
        Long expirationTime = System.currentTimeMillis() + 1000 * Long.parseLong(JWT_EXPIRATION);
        return generateToken(request, username, roles, expirationTime);
    }

    public static String generateRefreshToken(HttpServletRequest request, String username, List<String> roles) {
        return generateToken(request, username, roles, System.currentTimeMillis() + 30 * 1000 * Long.parseLong(JWT_EXPIRATION));
    }

    private static String generateToken(HttpServletRequest request, String username, List<String> roles, Long expiration) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(expiration))
                .withIssuer(request.getRequestURI())
                .withClaim("roles", roles)
                .sign(algorithm);
    }

}
