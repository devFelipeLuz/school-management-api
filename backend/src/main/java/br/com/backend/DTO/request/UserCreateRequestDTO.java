package br.com.backend.DTO.request;

import br.com.backend.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDTO {

    @NotBlank(message = "Username is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    private Role role;
}
