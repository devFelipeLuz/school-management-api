package br.com.backend.unit.entity;

import br.com.backend.builders.entity.*;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentTest {
    User user;
    Student student;
    SchoolYear year;
    Classroom classroom;
    Enrollment enrollment;

    @BeforeEach
    public void setup() {
        user = UserBuilder.builder()
                .withEmail("test@test.com")
                .withPassword("test123")
                .withRole(Role.STUDENT)
                .build();

        student = StudentBuilder.builder()
                .withName("Test")
                .withUser(user)
                .build();

        year = SchoolYearBuilder.builder()
                .withYear(2030)
                .build();

        classroom = ClassroomBuilder.builder()
                .withName("7.B")
                .withSchoolYear(year)
                .build();


        enrollment = EnrollmentBuilder.builder()
                .withStudent(student)
                .withClassroom(classroom)
                .build();

        enrollment.register();
    }

    @Test
    public void shouldCreateEnrollment() {
        assertEquals("Test", enrollment.getStudent().getName());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingEnrollmentWithNullStudent() {
        assertThrows(NullPointerException.class, () -> new Enrollment(null, classroom));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingEnrollmentWithNullClassroom() {
        assertThrows(NullPointerException.class, () -> new Enrollment(student, null));
    }

    @Test
    public void shouldCancelEnrollment() {
        enrollment.cancelEnrollment();
        assertTrue(enrollment.isCancelled());
    }

    @Test
    public void shouldThrowExceptionWhenCancellingEnrollmentAlreadyCancelled() {
        enrollment.cancelEnrollment();
        assertThrows(BusinessException.class, () -> enrollment.cancelEnrollment());
    }

    @Test
    public void shouldFinishEnrollment() {
        enrollment.finishEnrollment();
        assertTrue(enrollment.isFinished());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingEnrollmentWithInactiveStudent() {
        student.deactivate();
        assertThrows(BusinessException.class, () -> enrollment.finishEnrollment());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingEnrollmentWithInactiveSchoolYear() {
        year.deactivate();
        assertThrows(BusinessException.class, () -> enrollment.finishEnrollment());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingEnrollmentWithInactiveClassroom() {
        classroom.deactivate();
        assertThrows(BusinessException.class, () -> enrollment.finishEnrollment());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingCanceledEnrollment() {
        enrollment.cancelEnrollment();
        assertThrows(BusinessException.class, () -> enrollment.finishEnrollment());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingEnrollmentAlreadyFinished() {
        enrollment.finishEnrollment();
        assertThrows(BusinessException.class, () -> enrollment.finishEnrollment());
    }

    @Test
    public void shouldActivateEnrollment() {
        enrollment.cancelEnrollment();
        assertTrue(enrollment.isCancelled());

        enrollment.activateEnrollment();
        assertTrue(enrollment.isActive());
    }

    @Test
    public void shouldThrowExceptionWhenActivatingEnrollmentAlreadyActive() {
        assertThrows(BusinessException.class, () -> enrollment.activateEnrollment());
    }

    @Test
    public void shouldIncreaseActiveEnrollmentCountWhenActivatingCanceledEnrollment() {
        enrollment.cancelEnrollment();
        assertTrue(enrollment.isCancelled());
        assertEquals(0, enrollment.getClassroom().getEnrollmentCountForSchoolYear());

        enrollment.activateEnrollment();
        assertTrue(enrollment.isActive());
        assertEquals(1, enrollment.getClassroom().getEnrollmentCountForSchoolYear());
    }

    @Test
    public void shouldNotIncreaseActiveEnrollmentCountWhenActivatingFinishedEnrollment() {
        enrollment.finishEnrollment();
        assertTrue(enrollment.isFinished());
        assertEquals(1, enrollment.getClassroom().getEnrollmentCountForSchoolYear());

        enrollment.activateEnrollment();
        assertTrue(enrollment.isActive());
        assertEquals(1, enrollment.getClassroom().getEnrollmentCountForSchoolYear());
    }
}
