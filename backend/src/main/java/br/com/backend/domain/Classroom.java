package br.com.backend.domain;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
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

    public Classroom(String name, SchoolYear schoolYear) {
        this.name = name;
        this.maxCapacity = 25;
        this.schoolYear = schoolYear;
        this.activeEnrollmentsCount = 0;
        this.enrollments = new ArrayList<>();
    }

    public void validateCapacity() {
        if (activeEnrollmentsCount >= maxCapacity) {
            throw new BusinessException("Turma lotada");
        }
    }

    public void addEnrollment(Enrollment enrollment) {
        validateCapacity();
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

    public void cancelEnrollment(Enrollment enrollment) {
        enrollment.cancel();
        this.decreaseActiveEnrollmentsCount();
    }

    public void changeCapacity(int newCapacity) {
        this.maxCapacity = newCapacity;
    }
}
