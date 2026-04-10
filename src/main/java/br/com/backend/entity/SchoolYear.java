package br.com.backend.entity;

import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.Year;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class SchoolYear {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Integer year;

    @Column
    private Instant startDate;

    @Column
    private Instant endDate;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "active", nullable = false)
    private boolean active;

    public SchoolYear(Integer year) {
        this.year = validateYear(year);
        this.startDate = Instant.now();
        this.endDate = null;
        this.createdAt = Instant.now();
        this.active = true;
    }

    public void ensureInactive() {
        if (this.active) {
            throw new BusinessException("School year is active");
        }
    }

    public void ensureActive() {
        if (!this.active) {
            throw new BusinessException("School year is inactive");
        }
    }

    public void updateYear(Integer year) {
        ensureActive();
        this.year = validateYear(year);
    }

    public void activate() {
        ensureInactive();
        this.active = true;
    }

    public void deactivate() {
        ensureActive();
        this.active = false;
    }

    private Integer validateYear(Integer year) {
        if (year == null) {
            throw new BusinessException("Year cannot be null");
        }

        if (year <= 0) {
            throw new BusinessException("Year must be a positive number");
        }

        return year;
    }
}
