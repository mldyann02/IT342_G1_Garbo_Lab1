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

        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
                try {
                        AuthResponse resp = authService.register(req);
                        String token = resp.getToken();

                        ResponseCookie cookie = ResponseCookie.from("token", token)
                                        .httpOnly(true)
                                        .path("/")
                                        .maxAge(7 * 24 * 60 * 60)
                                        .sameSite("Lax")
                                        .secure(false)
                                        .build();

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                        .body(new AuthResponse(token, null));
                } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest()
                                        .body(new AuthResponse(null, e.getMessage()));
                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.internalServerError()
                                        .body(new AuthResponse(null, "Server error"));
                }
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
                AuthResponse resp = authService.login(req);
                String token = resp.getToken();

                ResponseCookie cookie = ResponseCookie.from("token", token)
                                .httpOnly(true)
                                .path("/")
                                .maxAge(7 * 24 * 60 * 60)
                                .sameSite("Lax")
                                .secure(false)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(resp);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout() {
                ResponseCookie cookie = ResponseCookie.from("token", "")
                                .httpOnly(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .build();
        }
}
