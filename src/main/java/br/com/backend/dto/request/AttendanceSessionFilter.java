package br.com.backend.dto.request;

public record AttendanceSessionFilter(
        String professorName,
        String subject,
        String classroomName,
        String date
) {
}
