package br.com.backend.domain;

import br.com.backend.domain.enums.EnrollmentStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "STUDENT_NAME")
    private String name;

    @Column(name = "STUDENT_EMAIL")
    private String email;

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;

    @Column(name = "ACTIVE")
    private boolean active;

    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.enrollments = new ArrayList<>();
        this.active = true;
    }

    public void saveData(String name, String email) {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }

        this.name = name;
        this.email = email;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean hasActiveEnrollment() {
        return enrollments.stream()
                .anyMatch(e -> e.getStatus() == EnrollmentStatus.ACTIVE);
    }

    public void validateCanEnroll() {
        if (!this.active) {
            throw new BusinessException("Aluno inativo");
        }

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
}
