package br.com.backend.util;

import br.com.backend.DTO.enrollment.EnrollmentResponseDTO;
import br.com.backend.DTO.grade.GradeResponseDTO;
import br.com.backend.DTO.student.StudentResponseDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.domain.Enrollment;
import br.com.backend.domain.Grade;
import br.com.backend.domain.Student;
import br.com.backend.domain.User;

public final class ToResponseDTO {

    private ToResponseDTO() {
        throw new UnsupportedOperationException("Classe de utilitários");
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    public static EnrollmentResponseDTO toEnrollmentResponseDTO(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getGrade(),
                enrollment.getStudent()
        );
    }

    public static GradeResponseDTO toGradeResponseDTO(Grade grade) {
        return new GradeResponseDTO(
                grade.getId(),
                grade.getName());
    }

    public static StudentResponseDTO toStudentResponseDTO(Student student) {
        String gradeName = student.getActiveEnrollments()
                .map(e -> e.getGrade().getName())
                .orElse(null);

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                gradeName
        );
    }
}
