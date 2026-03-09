package br.com.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class SchoolYear {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, updatable = false)
    private Instant startDate;

    private Instant endDate;

    @Column(name = "active", nullable = false)
    private boolean active;

    public SchoolYear(Integer year) {
        if (year == null || year <= 0) {
            throw new IllegalArgumentException("Year is null or blank");
        }

        this.year = year;
        this.startDate = Instant.now();
        this.endDate = null;
        this.active = true;
    }
}
