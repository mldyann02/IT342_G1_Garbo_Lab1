package com.citu.backend.controller;

import com.citu.backend.dto.AuthResponse;
import com.citu.backend.dto.LoginRequest;
import com.citu.backend.dto.RegisterRequest;
import com.citu.backend.service.AuthService;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final org.springframework.core.env.Environment env;

    public AuthController(AuthService authService, org.springframework.core.env.Environment env) {
        this.authService = authService;
        this.env = env;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        AuthResponse resp = authService.register(req);
        String token = resp.getToken();
        boolean prod = Arrays.asList(env.getActiveProfiles()).contains("prod");
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60);
        if (prod) {
            b.sameSite("None").secure(true);
        } else {
            b.sameSite("Lax").secure(false);
        }
        ResponseCookie cookie = b.build();
        // Do NOT return token in body to avoid exposing it in dev or prod
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new AuthResponse(null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        AuthResponse resp = authService.login(req);
        String token = resp.getToken();
        boolean prod = Arrays.asList(env.getActiveProfiles()).contains("prod");
        ResponseCookie.ResponseCookieBuilder b2 = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60);
        if (prod) {
            b2.sameSite("None").secure(true);
        } else {
            b2.sameSite("Lax").secure(false);
        }
        ResponseCookie cookie2 = b2.build();
        // Do NOT return token in body to avoid exposing it in dev or prod
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie2.toString()).body(new AuthResponse(null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
