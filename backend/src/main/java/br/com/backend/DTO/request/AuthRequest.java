package br.com.backend.DTO.request;

public record AuthRequest(
        String username,
        String password
) {}