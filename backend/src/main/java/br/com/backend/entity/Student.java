package br.com.backend.entity;

import br.com.backend.entity.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "student_name")
    private String name;

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

    @Column(name = "active")
    private boolean active;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Student(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name is null or blank");
        }

        this.name = name;
        this.enrollments = new ArrayList<>();
        this.active = true;
    }

    public void updateData(String name) {
        ensureActive();
        this.name = name;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean hasActiveEnrollment() {
        return enrollments.stream()
                .anyMatch(e -> e.getStatus() == EnrollmentStatus.ACTIVE);
    }

    public void validateCanEnroll() {
        ensureActive();
        if (hasActiveEnrollment()) {
            throw new BusinessException("Aluno já possui matrícula ativa");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public Optional<Enrollment> getActiveEnrollments() {
        return this.enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .findFirst();
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }
    }
}
