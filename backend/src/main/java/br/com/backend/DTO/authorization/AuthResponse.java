package br.com.backend.DTO.authorization;

public record AuthResponse (
        String accessToken,
        String refreshToken
){}
