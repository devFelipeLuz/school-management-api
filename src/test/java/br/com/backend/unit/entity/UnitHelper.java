package br.com.backend.unit.entity;

import br.com.backend.builders.entity.*;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.AssessmentType;

import java.time.LocalDate;

public class UnitHelper {

    public static User getStudentUser() {
        return UserBuilder.builder()
                .withEmail("student.test@unittest.com")
                .withPassword("unittest123")
                .build();
    }

    public static User getProfessorUser() {
        return UserBuilder.builder()
                .withEmail("professor.test@unittest.com")
                .withPassword("unittest123")
                .build();
    }

    public static Student getStudent() {
        return StudentBuilder.builder()
                .withName("Student Test")
                .withUser(getStudentUser())
                .build();
    }

    public static Professor getProfessor() {
        return ProfessorBuilder.builder()
                .withName("Professor Test")
                .withUser(getProfessorUser())
                .build();
    }

    public static SchoolYear getSchoolYear() {
        return SchoolYearBuilder.builder()
                .withYear(2030)
                .build();
    }

    public static Classroom getClassroom() {
        return ClassroomBuilder.builder()
                .withName("Classroom Test")
                .withSchoolYear(getSchoolYear())
                .build();
    }

    public static Enrollment getEnrollment() {
        return EnrollmentBuilder.builder()
                .withStudent(getStudent())
                .withClassroom(getClassroom())
                .build();
    }

    public static Subject getSubject() {
        return SubjectBuilder.builder()
                .withName("Subject Test")
                .build();
    }

    public static TeachingAssignment getAssignment() {
        return TeachingAssignmentBuilder.builder()
                .withProfessor(getProfessor())
                .withSubject(getSubject())
                .withClassroom(getClassroom())
                .build();
    }

    public static AttendanceSession getSession() {
        return AttendanceSessionBuilder.builder()
                .withAssignment(getAssignment())
                .withDate(LocalDate.parse("2030-10-05"))
                .build();
    }

    public static Assessment getAssessment() {
        return AssessmentBuilder.builder()
                .withTitle("Assessment Test")
                .withAssignment(getAssignment())
                .withType(AssessmentType.TEST)
                .build();
    }
}
