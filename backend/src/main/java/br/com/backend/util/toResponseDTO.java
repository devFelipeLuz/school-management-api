package br.com.backend.util;

import br.com.backend.DTO.enrollment.EnrollmentResponseDTO;
import br.com.backend.DTO.classroom.ClassroomResponseDTO;
import br.com.backend.DTO.student.StudentResponseDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.domain.*;

public final class ToResponseDTO {

    private ToResponseDTO() {
        throw new UnsupportedOperationException("Classe de utilitários");
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static EnrollmentResponseDTO toEnrollmentResponseDTO(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getStudent(),
                enrollment.getClassroom(),
                enrollment.getSchoolYear()
        );
    }

    public static ClassroomResponseDTO toClassroomResponseDTO(Classroom classroom) {
        return new ClassroomResponseDTO(
                classroom.getId(),
                classroom.getName());
    }

    public static StudentResponseDTO toStudentResponseDTO(Student student) {
        String gradeName = student.getActiveEnrollments()
                .map(e -> e.getClassroom().getName())
                .orElse(null);

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getUser().getEmail(),
                gradeName
        );
    }
}
