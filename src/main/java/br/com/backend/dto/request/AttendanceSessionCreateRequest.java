package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AttendanceSessionCreateRequest(
        @NotNull(message = "teaching_assignment_id is required")
        UUID teachingAssignmentId,

        @NotNull(message = "date is required")
        LocalDate date
) {
}
