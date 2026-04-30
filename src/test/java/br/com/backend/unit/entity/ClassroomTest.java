package br.com.backend.unit.entity;

import br.com.backend.builders.entity.ClassroomBuilder;
import br.com.backend.builders.entity.EnrollmentBuilder;
import br.com.backend.builders.entity.SchoolYearBuilder;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClassroomTest {
    SchoolYear year;
    Classroom classroom;

    @BeforeEach
    public void setUp() {
        year = SchoolYearBuilder.builder()
                .withYear(2029)
                .build();

        classroom = ClassroomBuilder.builder()
                .withName("7.C")
                .withSchoolYear(year)
                .build();
    }

    @Test
    public void shouldCreateClassroom() {
        assertNotNull(year);
        assertNotNull(classroom);
        assertEquals("7.C", classroom.getName());
        assertEquals(year, classroom.getSchoolYear());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingClassroomWithInactiveSchoolYear() {
        year.deactivate();
        assertFalse(year.isActive());

        assertThrows(BusinessException.class, () -> new Classroom("8.A", year));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingClassroomWithNullSchoolYear() {
        assertThrows(NullPointerException.class, () -> new Classroom("8.A", null));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingClassroomWithInvalidName() {
        assertThrows(BusinessException.class, () -> new Classroom(null, year));
        assertThrows(BusinessException.class, () -> new Classroom("", year));
        assertThrows(BusinessException.class, () -> new Classroom(" ", year));
    }

    @Test
    public void shouldChangeCapacity() {
        classroom.changeCapacity(0);
        assertEquals(0, classroom.getMaxCapacity());
    }

    @Test
    public void shouldThrowExceptionWhenChangingCapacityWithInvalidNewCapacity() {
        assertThrows(BusinessException.class, () -> classroom.changeCapacity(-1));
    }

    @Test
    public void shouldUpdateName() {
        classroom.updateName("6.B");
        assertEquals("6.B", classroom.getName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNameWithInvalidName() {
        assertThrows(BusinessException.class, () -> classroom.updateName(null));
        assertThrows(BusinessException.class, () -> classroom.updateName(""));
        assertThrows(BusinessException.class, () -> classroom.updateName(" "));
    }

    @Test
    public void shouldIncreaseActiveEnrollmentsCount() {
        classroom.increaseActiveEnrollmentsCount();
        assertEquals(1, classroom.getEnrollmentCountForSchoolYear());
    }

    @Test
    public void shouldDecreaseActiveEnrollmentsCount() {
        classroom.increaseActiveEnrollmentsCount();
        classroom.decreaseActiveEnrollmentsCount();
        assertEquals(0, classroom.getEnrollmentCountForSchoolYear());
    }

    @Test
    public void shouldThrowExceptionWhenDecreasingActiveEnrollmentsCountEqualsZero() {
        assertThrows(BusinessException.class, () -> classroom.decreaseActiveEnrollmentsCount());
    }

    @Test
    public void shouldThrowExceptionWhenIncreasingActiveEnrollmentsCountForLesserMaxCapacity() {
        classroom.changeCapacity(0);
        assertThrows(BusinessException.class, () -> classroom.increaseActiveEnrollmentsCount());
    }

    @Test
    public void shouldAddEnrollment() {
        Enrollment enrollment = EnrollmentBuilder.builder()
                .withClassroom(classroom)
                .build();

        classroom.addEnrollment(enrollment);

        assertEquals(1, classroom.getEnrollments().size());
        assertTrue(classroom.getActiveEnrollment().isPresent(), "Should exist active enrollment");
        assertEquals(enrollment, classroom.getActiveEnrollment().get());
    }

    @Test
    public void shouldThrowExceptionWhenAddingEnrollmentWithInactiveClassroom() {
        Enrollment enrollment = EnrollmentBuilder.builder()
                .withClassroom(classroom)
                .build();

        classroom.deactivate();
        assertThrows(BusinessException.class, () -> classroom.addEnrollment(enrollment));
    }

    @Test
    public void shouldThrowExceptionWhenAddingEnrollmentWithNullEnrollment() {
        assertThrows(BusinessException.class, () -> classroom.addEnrollment(null));
    }

    @Test
    public void shouldNotIncreaseActiveEnrollmentsCountWhenAddingCanceledEnrollment() {
        Enrollment enrollment = EnrollmentBuilder.builder()
                .withClassroom(classroom)
                .build();
        classroom.increaseActiveEnrollmentsCount();
        enrollment.cancelEnrollment();

        classroom.addEnrollment(enrollment);

        assertEquals(0, classroom.getEnrollmentCountForSchoolYear());
    }

    @Test
    public void shouldActivateClassroom() {
        classroom.deactivate();
        classroom.activate();
        assertTrue(classroom.isActive());
    }

    @Test
    public void shouldThrowExceptionWhenActivatingClassroomAlreadyActive() {
        assertThrows(BusinessException.class, () -> classroom.activate());
    }
}
