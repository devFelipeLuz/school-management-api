package br.com.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "teaching_assignments",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"professor_id", "subject_id", "classroom_id"})
})
public class TeachingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    public TeachingAssignment (Professor professor, Subject subject, Classroom classroom) {
        if (professor == null)  {
            throw new IllegalArgumentException("professor is null");
        }

        if (subject == null)  {
            throw new IllegalArgumentException("subject is null");
        }

        if (classroom == null)  {
            throw new IllegalArgumentException("classroom is null");
        }

        this.professor = professor;
        this.subject = subject;
        this.classroom = classroom;
    }
}
