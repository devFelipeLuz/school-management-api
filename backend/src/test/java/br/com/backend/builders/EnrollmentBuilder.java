package br.com.backend.builders;

import br.com.backend.entity.Classroom;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.SchoolYear;
import br.com.backend.entity.Student;

public class EnrollmentBuilder {

    private Student student = StudentBuilder.builder().build();
    private Classroom classroom = ClassroomBuilder.builder().build();
    private SchoolYear schoolYear = SchoolYearBuilder.builder().build();

    public static EnrollmentBuilder builder() {
        return new EnrollmentBuilder();
    }

    public EnrollmentBuilder withStudent(Student student) {
        this.student = student;
        return this;
    }

    public EnrollmentBuilder withSchoolYear(SchoolYear schoolYear) {
        this.schoolYear = schoolYear;
        return this;
    }

    public EnrollmentBuilder withClassroom(Classroom classroom) {
        this.classroom = classroom;
        return this;
    }

    public Enrollment build() {
        return new Enrollment(student, schoolYear, classroom);
    }
}
