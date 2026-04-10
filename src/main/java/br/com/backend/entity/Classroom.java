package br.com.backend.entity;

import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "classroom",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "school_year_id"})
    }
)
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "school_year_id")
    private SchoolYear schoolYear;

    @Column(name = "active_enrollments", nullable = false)
    private int enrollmentCountForSchoolYear;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments =  new ArrayList<>();

    @Column(name = "active", nullable = false)
    private boolean active;

    public Classroom(String name, SchoolYear schoolYear) {
        schoolYear.ensureActive();
        this.name = validateName(name);
        this.maxCapacity = 25;
        this.schoolYear = Objects.requireNonNull(schoolYear, "School year cannot be null");
        this.enrollmentCountForSchoolYear = 0;
        this.active = true;
    }

    public void ensureCapacity() {
        if (isEnrollmentsCountIsGreaterOrEqualsThanMaxCapacity()) {
            throw new BusinessException("Classroom is full");
        }
    }

    private boolean isEnrollmentsCountIsGreaterOrEqualsThanMaxCapacity() {
        return enrollmentCountForSchoolYear >= maxCapacity;
    }

    public void changeCapacity(int newCapacity) {
        ensureActive();
        ensureNewCapacity(newCapacity);
        this.maxCapacity = newCapacity;
    }

    public void updateName(String name) {
        this.name = validateName(name);
    }

    private boolean isNewCapacityLesserThanEnrollmentsCount(int newCapacity) {
        return newCapacity < enrollmentCountForSchoolYear;
    }

    private void ensureNewCapacity(int newCapacity) {
        if (isNewCapacityLesserThanEnrollmentsCount(newCapacity)) {
            throw new BusinessException("The new capacity cannot be less than active enrollments");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        ensureActive();
        throwsExceptionWhenEnrollmentIsNull(enrollment);

        this.enrollments.add(enrollment);

        if (isEnrollmentActive(enrollment)) {
            increaseActiveEnrollmentsCount();
        }
    }

    public void increaseActiveEnrollmentsCount() {
        ensureCapacity();
        this.enrollmentCountForSchoolYear++;
    }

    public void decreaseActiveEnrollmentsCount() {
        ensureActive();
        ensureEnrollmentsCountIsGreaterThanZero();
        this.enrollmentCountForSchoolYear--;
    }

    private boolean isEnrollmentsCountEqualsZero() {
        return enrollmentCountForSchoolYear == 0;
    }

    private void ensureEnrollmentsCountIsGreaterThanZero() {
        if (isEnrollmentsCountEqualsZero()) {
            throw new BusinessException("No active enrollments to remove");
        }
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public void ensureInactive() {
        if (this.active) {
            throw new BusinessException("Classroom is active");
        }
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Classroom is not active");
        }
    }

    public void activate() {
        ensureInactive();
        this.active = true;
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Name cannot be null or blank");
        }

        return name;
    }

    private boolean isEnrollmentNull(Enrollment enrollment) {
        return enrollment == null;
    }

    private void throwsExceptionWhenEnrollmentIsNull(Enrollment enrollment) {
        if (isEnrollmentNull(enrollment)) {
            throw new BusinessException("Enrollment cannot be null");
        }
    }

    private boolean isEnrollmentActive(Enrollment enrollment) {
        return enrollment.getStatus() == EnrollmentStatus.ACTIVE;
    }
}
