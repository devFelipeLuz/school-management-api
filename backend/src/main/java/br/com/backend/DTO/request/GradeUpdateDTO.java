package br.com.backend.DTO.request;

import jakarta.validation.constraints.NotNull;

public record GradeUpdateDTO(
        @NotNull Double grade
) {}
