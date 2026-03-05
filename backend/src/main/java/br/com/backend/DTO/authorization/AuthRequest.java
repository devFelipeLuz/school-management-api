package br.com.backend.DTO.authorization;

public record AuthRequest(
        String username,
        String password
) {}