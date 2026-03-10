package br.com.backend.DTO.assessment;

import br.com.backend.domain.StudentGrade;
import br.com.backend.domain.TeachingAssignment;
import br.com.backend.domain.enums.AssessmentType;

public record AssessmentRequestDTO (
        String title,
        AssessmentType type,
        TeachingAssignment teachingAssignment,
        StudentGrade studentGrade
    ) {
}
