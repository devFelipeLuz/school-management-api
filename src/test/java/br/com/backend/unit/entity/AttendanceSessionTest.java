package br.com.backend.unit.entity;

import br.com.backend.builders.entity.*;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.entity.enums.Role;

import static org.junit.jupiter.api.Assertions.*;

import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class AttendanceSessionTest {

    User professorUser;
    User studentUser;

    Professor professor;
    Student student;

    Subject subject;

    SchoolYear year;
    Classroom classroom;

    TeachingAssignment assignment;

    Enrollment enrollment;

    AttendanceSession session;


    @BeforeEach
    public void setUp() {
        professorUser = UserBuilder.builder()
                .withEmail("professor.test@test.com")
                .withPassword("test123")
                .withRole(Role.PROFESSOR)
                .build();

        studentUser = UserBuilder.builder()
                .withEmail("student.test@test.com")
                .withPassword("test123")
                .withRole(Role.STUDENT)
                .build();

        professor = ProfessorBuilder.builder()
                .withName("Professor Test")
                .withUser(professorUser)
                .build();

        student = StudentBuilder.builder()
                .withName("Student Test")
                .withUser(studentUser)
                .build();

        subject = SubjectBuilder.builder()
                .withName("Chemistry")
                .build();

        year = SchoolYearBuilder.builder()
                .withYear(2030)
                .build();

        classroom = ClassroomBuilder.builder()
                .withSchoolYear(year)
                .withName("7.B")
                .build();

        assignment = TeachingAssignmentBuilder.builder()
                .withProfessor(professor)
                .withSubject(subject)
                .withClassroom(classroom)
                .build();

        enrollment = EnrollmentBuilder.builder()
                .withStudent(student)
                .withClassroom(classroom)
                .build();
        enrollment.register();

        session = AttendanceSessionBuilder.builder()
                .withAssignment(assignment)
                .withDate(LocalDate.parse("2030-10-05"))
                .build();
    }

    @Test
    public void shouldCreateSession() {
        assertEquals("2030-10-05", session.getDate().toString());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingSessionWithNullAssignment() {
        assertThrows(NullPointerException.class,
                () -> new AttendanceSession(null, LocalDate.parse("2030-11-05")));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingSessionWithNullDate() {
        assertThrows(NullPointerException.class,
                () -> new AttendanceSession(assignment, null));
    }

    @Test
    public void shouldRegisterAttendance() {
        session.registerAttendance(enrollment, AttendanceStatus.PRESENT);
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringAttendanceWithInactiveSession() {
        session.deactivate();

        assertThrows(BusinessException.class, () -> session.registerAttendance(enrollment, AttendanceStatus.PRESENT));
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringAttendanceWithEnrollmentThatNotBelongsToAssignment() {
        User tempUser = new User("temp.student.test@test.com", "test123", Role.STUDENT);
        Student studentTest = new Student("temp student", tempUser);
        Classroom classroomTest = new Classroom("6.B", year);

        Enrollment enrollmentTest = EnrollmentBuilder.builder()
                .withStudent(studentTest)
                .withClassroom(classroomTest)
                .build();
        enrollmentTest.register();

        assertThrows(BusinessException.class,
                () -> session.registerAttendance(enrollmentTest, AttendanceStatus.PRESENT));
    }

    @Test
    public void shouldThrowExceptionWhenRegisteringAttendanceAlreadyRegistered() {
        session.registerAttendance(enrollment, AttendanceStatus.PRESENT);
        assertThrows(BusinessException.class, () -> session.registerAttendance(enrollment, AttendanceStatus.PRESENT));
    }
}
