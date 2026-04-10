package br.com.backend.dto.response;

import br.com.backend.entity.enums.EnrollmentStatus;

import java.time.Instant;
import java.util.UUID;

public record EnrollmentResponseDTO(
        UUID id,
        String studentName,
        Integer schoolYear,
        String classroomName,
        Instant enrolledAt,
        EnrollmentStatus status
) {}
