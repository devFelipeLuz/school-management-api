package br.com.backend.mapper;

import br.com.backend.dto.response.AttendanceRecordResponseDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.TeachingAssignment;

import java.util.List;

public class AttendanceSessionMapper {
    
    private AttendanceSessionMapper() {
        throw new IllegalArgumentException("Mapper");
    }
    
    public static AttendanceSessionResponseDTO toDTO(AttendanceSession session) {
        TeachingAssignment assingment = session.getTeachingAssignment();

        List<AttendanceRecordResponseDTO> students = session.getRecords().stream()
                .map(r -> new AttendanceRecordResponseDTO(
                        r.getId(),
                        r.getEnrollment().getStudent().getName(),
                        r.getStatus()
                ))
                .toList();

        return new AttendanceSessionResponseDTO(
                session.getId(),
                assingment.getProfessor().getName(),
                assingment.getSubject().getName(),
                assingment.getClassroom().getName(),
                session.getDate(),
                students
        );
    }
}
