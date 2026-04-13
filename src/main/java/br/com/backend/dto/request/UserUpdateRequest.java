package br.com.backend.dto.request;

import br.com.backend.entity.enums.Role;

public record UserUpdateRequest(
        String email,
        String password,
        Role role
) {
}
