package br.com.backend.dto.response;

import br.com.backend.entity.enums.AttendanceStatus;

import java.util.UUID;

public record AttendanceRecordResponseDTO(
        UUID recordId,
        String studentName,
        AttendanceStatus status
) {}
