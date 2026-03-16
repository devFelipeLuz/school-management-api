package br.com.backend.dto.response;

import java.util.UUID;

public record EnrollmentResponseDTO(
        UUID id,
        String studentName,
        Integer schoolYearName,
        String classroomName
) {}
