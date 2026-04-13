package br.com.backend.dto.response;

import br.com.backend.entity.enums.Role;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String email,
        Instant createdAt,
        Role role,
        Boolean enabled
) {}
