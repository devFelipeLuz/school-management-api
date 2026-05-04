package br.com.backend.unit.entity;

import br.com.backend.builders.entity.AssessmentBuilder;
import br.com.backend.builders.entity.EnrollmentBuilder;
import br.com.backend.builders.entity.TeachingAssignmentBuilder;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentGradeTest {

    Student student;
    Professor professor;
    Subject subject;
    Classroom classroom;
    TeachingAssignment assignment;
    Assessment assessment;
    Enrollment enrollment;
    StudentGrade grade;

    @BeforeEach
    public void setUp() {
        student = UnitHelper.getStudent();
        professor = UnitHelper.getProfessor();
        subject = UnitHelper.getSubject();
        classroom = UnitHelper.getClassroom();

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

        assessment = AssessmentBuilder.builder()
                .withTitle("Any Title")
                .withAssignment(assignment)
                .withType(AssessmentType.TEST)
                .build();

        grade = new StudentGrade(assessment, enrollment, 10.0);
    }

    @Test
    public void shouldCreateStudentGrade() {
        assertNotNull(grade);
        assertEquals(assessment, grade.getAssessment());
    }

    @Test
    public void shouldNotCreateStudentGradeWhenAssessmentHasDifferentClassroomThanEnrollment() {
        Enrollment enrollmentTest = UnitHelper.getEnrollment();
        assertThrows(BusinessException.class, () -> new StudentGrade(assessment, enrollmentTest, 10.0));
    }

    @Test
    public void shouldNotCreateStudentGradeWhenAssessmentIsNull() {
        assertThrows(NullPointerException.class, () -> new StudentGrade(null, enrollment, 10.0));
    }

    @Test
    public void shouldNotCreateStudentGradeWhenEnrollmentIsNull() {
        assertThrows(NullPointerException.class, () -> new StudentGrade(assessment, null, 10.0));
    }

    @Test
    public void shouldUpdateGrade() {
        grade.updateGrade(2.0);
        assertEquals(2.0, grade.getGrade());
    }

    @Test
    public void shouldNotUpdateGradeWhenNewGradeIsInvalid() {
        assertThrows(BusinessException.class, () -> grade.updateGrade(null));
        assertThrows(BusinessException.class, () -> grade.updateGrade(20.0));
        assertThrows(BusinessException.class, () -> grade.updateGrade(-10.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveAssessment() {
        assessment.deactivate();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveEnrollment() {
        enrollment.cancelEnrollment();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveStudent() {
        enrollment.getStudent().deactivate();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveClassroom() {
        enrollment.getClassroom().deactivate();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveSchoolYear() {
        enrollment.getClassroom().getSchoolYear().deactivate();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveProfessor() {
        assessment.getTeachingAssignment().getProfessor().deactivate();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }

    @Test
    public void shouldNotUpdateGradeWithInactiveSubject() {


        assessment.getTeachingAssignment().getSubject().deactivate();
        assertThrows(BusinessException.class, () -> grade.updateGrade(2.0));
    }
}
