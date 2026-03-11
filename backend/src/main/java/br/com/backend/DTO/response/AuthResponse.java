package br.com.backend.DTO.response;

public record AuthResponse (
        String accessToken,
        String refreshToken
){}
