package com.romankliuiev.socialnetwork.config;

import com.romankliuiev.socialnetwork.config.filter.CustomAuthenticationFilter;
import com.romankliuiev.socialnetwork.config.filter.CustomAuthorizationFilter;
import com.romankliuiev.socialnetwork.service.InactiveTokenService;
import com.romankliuiev.socialnetwork.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder encoder;
    private final InactiveTokenService inactiveTokenService;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, UserService userService, AuthenticationManagerBuilder auth, InactiveTokenService inactiveTokenService) throws Exception {
        this.authenticationConfiguration = authenticationConfiguration;
        this.inactiveTokenService = inactiveTokenService;
        this.encoder = new BCryptPasswordEncoder();
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationConfiguration.getAuthenticationManager());
        filter.setFilterProcessesUrl("/api/v1/login");
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeHttpRequests((auth) -> {
                            try {
                                auth
//                                        .requestMatchers(HttpMethod.POST, "/*").permitAll()
//                                        .anyRequest().authenticated()
                                        .anyRequest().permitAll()
                                        .and()
                                        .cors()
                                        .and()
                                        .addFilter(filter)
                                        .addFilterBefore(new CustomAuthorizationFilter(inactiveTokenService), UsernamePasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return encoder;
    }

}