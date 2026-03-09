package br.com.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    public Subject(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Subject name cannot be null or blank");
        }

        this.name = name;
    }
}
