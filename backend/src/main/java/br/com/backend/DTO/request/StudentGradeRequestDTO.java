package br.com.backend.DTO.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentGradeRequestDTO(
        @NotNull UUID assessmentId,
        @NotNull UUID enrollmentId
) {}
