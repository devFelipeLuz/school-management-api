package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "student_grade",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"enrollment_id", "assessment_id"})
        })
public class StudentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private Double grade;

    @Column(nullable = false)
    private Double maxScore;

    public StudentGrade(Assessment assessment, Enrollment enrollment, Double maxScore) {
        ensureSameClassroom(assessment, enrollment);
        this.assessment = Objects.requireNonNull(assessment, "Assessment cannot be null");
        this.enrollment = Objects.requireNonNull(enrollment, "Enrollment cannot be null");
        this.grade = null;
        this.maxScore = maxScore;
    }

    public void updateGrade(Double grade) {
        ensureGrade(grade);
        ensureOperational();
        this.grade = grade;
    }

    private void ensureAssessmentIsActive() {
        if (!this.assessment.isActive()) {
            throw new BusinessException("Assessment is not active");
        }
    }

    private void ensureEnrollmentIsActive() {
        if (!this.enrollment.isActive()) {
            throw new BusinessException("Enrollment is not active");
        }
    }

    private void ensureStudentIsActive() {
        if (!this.enrollment.getStudent().isActive()) {
            throw new BusinessException("Student is not active");
        }
    }

    private void ensureClassroomIsActive() {
        if (!this.enrollment.getClassroom().isActive()) {
            throw new BusinessException("Classroom is not active");
        }
    }

    private void ensureSchoolYearIsActive() {
        if (!this.enrollment.getSchoolYear().isActive()) {
            throw new BusinessException("School year is not active");
        }
    }

    private void ensureProfessorIsActive() {
        if (!assessment.getTeachingAssignment().getProfessor().isActive()) {
            throw new BusinessException("Professor is not active");
        }
    }

    private void ensureSubjectIsActive() {
        if (!assessment.getTeachingAssignment().getSubject().isActive()) {
            throw new BusinessException("Subject is not active");
        }
    }

    public void ensureOperational() {
        ensureAssessmentIsActive();
        ensureEnrollmentIsActive();
        ensureStudentIsActive();
        ensureClassroomIsActive();
        ensureSchoolYearIsActive();
        ensureProfessorIsActive();
        ensureSubjectIsActive();
    }

    private void ensureSameClassroom(Assessment assessment, Enrollment enrollment) {
        if (!assessment.getTeachingAssignment().getClassroom()
                .equals(enrollment.getClassroom())) {
            throw new BusinessException("Student is not enrolled in the classroom of this assessment");
        }
    }

    private void ensureGrade(Double grade) {
        if (grade == null) {
            throw new BusinessException("Grade cannot be null");
        }

        if (grade < 0 || grade > this.maxScore) {
            throw new BusinessException("Grade cannot be less than 0 or greater than max score");
        }
    }
}
