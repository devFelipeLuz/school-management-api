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
@Table(name = "classroom")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
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
        if (validateEnrollmentsCountIsGreaterOrEqualsThanMaxCapacity()) {
            throw new BusinessException("Classroom is full");
        }
    }

    private boolean validateEnrollmentsCountIsGreaterOrEqualsThanMaxCapacity() {
        return enrollmentCountForSchoolYear >= maxCapacity;
    }

    public void changeCapacity(int newCapacity) {
        ensureActive();
        ensureNewCapacity(newCapacity);
        this.maxCapacity = newCapacity;
    }

    private boolean validateNewCapacityIsLessThanEnrollmentsCount(int newCapacity) {
        return newCapacity < enrollmentCountForSchoolYear;
    }

    private void ensureNewCapacity(int newCapacity) {
        if (validateNewCapacityIsLessThanEnrollmentsCount(newCapacity)) {
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

    private boolean validateEnrollmentsCountEqualsZero() {
        return enrollmentCountForSchoolYear == 0;
    }

    private void ensureEnrollmentsCountIsGreaterThanZero() {
        if (validateEnrollmentsCountEqualsZero()) {
            throw new BusinessException("No active enrollments to remove");
        }
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Classroom is not active");
        }
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }

    private String validateName(String name) {
        ensureNameIsNotNullOrBlank(name);
        return name;
    }

    private boolean nameIsNotNullOrBlank(String name) {
        return name != null && !name.isBlank();
    }

    private void ensureNameIsNotNullOrBlank(String name) {
        if (!nameIsNotNullOrBlank(name)) {
            throw new BusinessException("Name cannot be null or blank");
        }
    }

    private boolean ensureEnrollmentIsNotNull(Enrollment enrollment) {
        return enrollment != null;
    }

    private void throwsExceptionWhenEnrollmentIsNull(Enrollment enrollment) {
        if (!ensureEnrollmentIsNotNull(enrollment)) {
            throw new BusinessException("Enrollment cannot be null");
        }
    }

    private boolean isEnrollmentActive(Enrollment enrollment) {
        return enrollment.getStatus() == EnrollmentStatus.ACTIVE;
    }
}
