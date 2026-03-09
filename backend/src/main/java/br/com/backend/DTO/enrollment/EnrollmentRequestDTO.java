package br.com.backend.DTO.enrollment;

import br.com.backend.domain.Classroom;
import br.com.backend.domain.SchoolYear;
import br.com.backend.domain.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnrollmentRequestDTO {

    @NotBlank(message = "Student is required")
    private Student student;

    @NotBlank(message = "Grade is required")
    private Classroom classroom;

    @NotBlank(message = "SchoolYear is required")
    private SchoolYear schoolYear;
}
