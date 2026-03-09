package br.com.backend.DTO.enrollment;


import br.com.backend.domain.Classroom;
import br.com.backend.domain.SchoolYear;
import br.com.backend.domain.Student;
import lombok.Getter;

import java.util.UUID;

@Getter
public class EnrollmentResponseDTO {

    private UUID id;

    private Student student;

    private Classroom classroom;

    private SchoolYear schoolYear;

    public EnrollmentResponseDTO(UUID id, Student student, Classroom classroom, SchoolYear schoolYear) {
        this.id = id;
        this.student = student;
        this.classroom = classroom;
        this.schoolYear = schoolYear;
    }
}
