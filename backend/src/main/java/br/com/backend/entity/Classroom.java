package br.com.backend.entity;

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

    @Column(name = "classroom_capacity")
    private int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "school_year_id")
    private SchoolYear schoolYear;

    @Column(name = "active_enrollments")
    private int activeEnrollmentsCount;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    @Column(name = "active")
    private boolean active;

    public Classroom(String name, SchoolYear schoolYear) {
        schoolYear.ensureActive();
        this.name = validateName(name);
        this.maxCapacity = 25;
        this.schoolYear = Objects.requireNonNull(schoolYear, "School year cannot be null");
        this.activeEnrollmentsCount = 0;
        this.enrollments = new ArrayList<>();
        this.active = true;
    }

    public void ensureCapacity() {
        if (activeEnrollmentsCount >= maxCapacity) {
            throw new BusinessException("Classroom is full");
        }
    }

    public void changeCapacity(int newCapacity) {
        if (newCapacity < activeEnrollmentsCount) {
            throw new BusinessException("The new capacity cannot be less than active enrollments");
        }

        this.maxCapacity = newCapacity;
    }

    public void addEnrollment(Enrollment enrollment) {
        if (enrollment == null) {
            throw new BusinessException("Enrollment cannot be null");
        }

        this.enrollments.add(enrollment);
        increaseActiveEnrollmentsCount();
    }

    public void increaseActiveEnrollmentsCount() {
        if (activeEnrollmentsCount >= maxCapacity) {
            throw new BusinessException("Turma lotada");
        }

        this.activeEnrollmentsCount++;
    }

    public void decreaseActiveEnrollmentsCount() {
        if (activeEnrollmentsCount == 0) {
            throw new IllegalArgumentException("Turma vazia");
        }

        this.activeEnrollmentsCount--;
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public void deactivate() {
        this.active = false;
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Classroom is not active");
        }
    }

    public String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Name cannot be null or blank");
        }

        return name;
    }
}
