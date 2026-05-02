package br.com.backend.unit.entity;

import br.com.backend.builders.entity.*;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssessmentTest {

    User user;
    Professor professor;
    Subject subject;
    SchoolYear year;
    Classroom classroom;
    TeachingAssignment assignment;
    Assessment assessment;

    @BeforeEach
    public void setup() {
        user = UserBuilder.builder()
                .withEmail("professor.test@test.com")
                .withPassword("test123")
                .withRole(Role.PROFESSOR)
                .build();

        professor = ProfessorBuilder.builder()
                .withName("Professor Test")
                .withUser(user)
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

        assessment = AssessmentBuilder.builder()
                .withAssignment(assignment)
                .withTitle("Assessment Test")
                .withType(AssessmentType.TEST)
                .build();
    }

    @Test
    public void shouldCreateAssessment() {
        assertEquals("Assessment Test", assessment.getTitle());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingAssessmentWithInvalidTitle() {
        assertThrows(BusinessException.class, () -> new Assessment(assignment, null, AssessmentType.PROJECT));
        assertThrows(BusinessException.class, () -> new Assessment(assignment, "", AssessmentType.PROJECT));
        assertThrows(BusinessException.class, () -> new Assessment(assignment, " ", AssessmentType.PROJECT));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingAssessmentWithInvalidType() {
        assertThrows(BusinessException.class, () -> new Assessment(assignment, "Any Title", null));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingAssessmentWithNullAssignment() {
        assertThrows(NullPointerException.class, () -> new Assessment(null, assessment.getTitle(), AssessmentType.PROJECT));
    }

    @Test
    public void shouldUpdateTitle() {
        assessment.updateTitle("Any Title");
        assertEquals("Any Title", assessment.getTitle());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingTitleWithInvalidInactiveAssessment() {
        assessment.deactivate();
        assertThrows(BusinessException.class, () -> assessment.updateTitle("Any Title"));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingTitleWithInvalidTitle() {
        assertThrows(BusinessException.class, () -> assessment.updateTitle(null));
        assertThrows(BusinessException.class, () -> assessment.updateTitle(""));
        assertThrows(BusinessException.class, () -> assessment.updateTitle(" "));
    }

    @Test
    public void shouldUpdateType() {
        assessment.updateType(AssessmentType.HOMEWORK);
        assertEquals(AssessmentType.HOMEWORK, assessment.getType());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingTypeWithInactiveAssessment() {
        assessment.deactivate();
        assertThrows(BusinessException.class, () -> assessment.updateType(AssessmentType.HOMEWORK));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingTypeWithInvalidType() {
        assertThrows(BusinessException.class, () -> assessment.updateType(null));
    }

    @Test
    public void shouldDeactivateAssessment() {
        assessment.deactivate();
        assertFalse(assessment.isActive());
    }

    @Test
    public void shouldThrowExceptionWhenDeactivatingAssessmentAlreadyInactive() {
        assessment.deactivate();
        assertThrows(BusinessException.class, () -> assessment.deactivate());
    }

    @Test
    public void shouldActivateAssessment() {
        assessment.deactivate();
        assertFalse(assessment.isActive());

        assessment.activate();
        assertTrue(assessment.isActive());
    }

    @Test
    public void shouldThrowExceptionWhenActivatingAssessmentAlreadyActive() {
        assertThrows(BusinessException.class, () -> assessment.activate());
    }
}
