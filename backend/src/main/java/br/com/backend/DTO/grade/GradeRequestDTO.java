package br.com.backend.DTO.grade;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class GradeRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;
}
