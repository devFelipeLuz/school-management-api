package br.com.backend.dto.request;

import br.com.backend.entity.enums.AttendanceStatus;

public record AttendanceRecordUpdateRequest(
        AttendanceStatus status
) {
}
