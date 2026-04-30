package br.com.backend.unit.entity;

import br.com.backend.builders.entity.EnrollmentBuilder;
import br.com.backend.builders.entity.StudentBuilder;
import br.com.backend.builders.entity.UserBuilder;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.Role;

import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    User user;
    Student student;

    @BeforeEach
    public void setUp() {
        user = UserBuilder.builder()
                .withEmail("test@test.com")
                .withPassword("test123")
                .withRole(Role.STUDENT)
                .build();

        student = StudentBuilder.builder()
                .withName("Test Testing")
                .withUser(user)
                .build();
    }

    @Test
    public void shouldCreateStudent() {
        assertNotNull(user);
        assertNotNull(student.getUser().getEmail());
        assertEquals(user.getEmail(), student.getUser().getEmail());

        assertNotNull(student);
        assertEquals("Test Testing", student.getName());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingStudentWithNullUser() {
        assertThrows(NullPointerException.class, () -> new Student("Second Student Test", null));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingStudentWithInvalidName() {
        User userTest = UserBuilder.builder()
                .withEmail("usertest@test.com")
                .withPassword("usertest123")
                .withRole(Role.STUDENT)
                .build();

        assertThrows(BusinessException.class, () -> new Student(" ", userTest));
        assertThrows(BusinessException.class, () -> new Student("", userTest));
        assertThrows(BusinessException.class, () -> new Student(null, userTest));
    }

    @Test
    public void shouldAddEnrollmentToStudent() {
        Enrollment enrollment = EnrollmentBuilder.builder()
                .withStudent(student)
                .build();

        student.addEnrollment(enrollment);

        assertTrue(student.hasActiveEnrollment(), "Student should have one active enrollment");
        assertTrue(student.getActiveEnrollment().isPresent(), "Should exist active enrollment");
        assertEquals(enrollment, student.getActiveEnrollment().get());
    }

    @Test
    public void shouldThrowExceptionWhenAddEnrollmentToInactiveStudent() {
        Enrollment enrollment = EnrollmentBuilder.builder()
                .withStudent(student)
                .build();

        student.deactivate();

        assertThrows(BusinessException.class, () -> student.addEnrollment(enrollment));
    }

    @Test
    public void shouldThrowExceptionWhenAddEnrollmentToStudentThatAlreadyHasActiveEnrollment() {
        Enrollment enrollment = EnrollmentBuilder.builder()
                .withStudent(student)
                .build();

        student.addEnrollment(enrollment);

        assertTrue(student.hasActiveEnrollment(), "Student should have one active enrollment");
        assertTrue(student.getActiveEnrollment().isPresent(), "Should exist active enrollment");
        assertEquals(enrollment, student.getActiveEnrollment().get());

        Enrollment enrollmentTest = EnrollmentBuilder.builder()
                .withStudent(student)
                .build();

        assertThrows(BusinessException.class, () -> student.addEnrollment(enrollmentTest));
    }

    @Test
    public void shouldUpdateStudentName() {
        student.updateName("Rodrigo Santoro");
        assertEquals("Rodrigo Santoro", student.getName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNameOfInactiveStudent() {
        student.deactivate();
        assertFalse(student.isActive());
        assertThrows(BusinessException.class, () -> student.updateName("Rodrigo Santoro"));
    }

    @Test
    public void shouldDeactivateStudent() {
        student.deactivate();
        assertFalse(student.isActive());
    }

    @Test
    public void shouldThrowExceptionWhenDeactivatingStudentAlreadyInactive() {
        student.deactivate();
        assertFalse(student.isActive());
        assertThrows(BusinessException.class, () -> student.deactivate());
    }

    @Test
    public void shouldActivateStudent() {
        student.deactivate();
        assertFalse(student.isActive());

        student.activate();
        assertTrue(student.isActive());
    }

    @Test
    public void shouldThrowExceptionWhenActivatingStudentAlreadyActive() {
        assertThrows(BusinessException.class, () -> student.activate());
    }
}
