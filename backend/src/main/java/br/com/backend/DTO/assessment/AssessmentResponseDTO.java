package br.com.backend.DTO.assessment;

import java.util.UUID;

public record AssessmentResponseDTO(
        UUID id,
        String title,
        String subject,
        String type,
        String professorName,
        String classroom,
        String grade
) {}
