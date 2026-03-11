package br.com.backend.DTO.response;

import java.util.UUID;

public record ProfessorResponseDTO(
        UUID id,
        String name,
        String username
) {}
