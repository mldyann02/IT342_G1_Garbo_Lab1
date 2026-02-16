package com.citu.backend.dto;

public class AuthResponse {

    private String token;
    private String error; // new field to send error messages

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
        this.error = null;
    }

    public AuthResponse(String token, String error) {
        this.token = token;
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
