package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnrollmentRequest(
        @NotNull (message = "StudentID is required")
        UUID studentId,

        @NotNull (message = "ClassroomID is required")
        UUID classroomId
) {}
