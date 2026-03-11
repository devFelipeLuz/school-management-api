package br.com.backend.DTO.response;

import br.com.backend.entity.enums.AssessmentType;

import java.time.Instant;
import java.util.UUID;

public record AssessmentResponseDTO(
        UUID id,
        String title,
        String subject,
        AssessmentType type,
        String professorName,
        String classroom,
        Instant date
) {}
