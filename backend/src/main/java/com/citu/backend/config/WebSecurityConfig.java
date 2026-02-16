package com.citu.backend.config;

import com.citu.backend.security.JwtFilter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;
    private final Environment env;

    public WebSecurityConfig(JwtFilter jwtFilter, Environment env) {
        this.jwtFilter = jwtFilter;
        this.env = env;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        String frontend = System.getenv("FRONTEND_ORIGIN");
        if (frontend == null || frontend.isBlank()) {
            // No frontend env configured -- allow common dev origins and emulator
            // Use origin patterns to be more flexible in dev
            config.setAllowedOriginPatterns(
                    Arrays.asList("http://localhost:*", "http://10.0.2.2:*", "http://10.0.2.2", "http://127.0.0.1:*"));
        } else {
            // When FRONTEND_ORIGIN is set, allow it plus emulator host for mobile testing
            config.setAllowedOriginPatterns(Arrays.asList(frontend != null ? frontend : "*", "http://10.0.2.2:*"));
        }
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // boolean prod = Arrays.asList(env.getActiveProfiles()).contains("prod");

    // if (prod) {
    // http.csrf(csrf -> csrf
    // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    // .ignoringRequestMatchers("/api/auth/**"));
    // } else {
    // http.csrf(csrf -> csrf.disable());
    // }

    // http
    // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    // .sessionManagement(session ->
    // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    // .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
    // .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
    // .requestMatchers("/api/auth/**").permitAll()
    // .anyRequest().authenticated())
    // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // http.exceptionHandling(e -> e
    // .authenticationEntryPoint((req, res, ex) -> {
    // res.setContentType("application/json");
    // res.setStatus(HttpServletResponse.SC_FORBIDDEN);
    // res.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    // }));

    // return http.build();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // disable CSRF completely for dev
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
