package br.com.backend.domain;

import br.com.backend.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "attendance_session",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teaching_assignment_id", "date"})
    }
)
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assignment_id", nullable = false)
    private TeachingAssignment teachingAssignment;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "attendanceSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttendanceRecord> records = new HashSet<>();

    public AttendanceSession(TeachingAssignment teachingAssignment, LocalDate date) {
        if (teachingAssignment == null) {
            throw new IllegalArgumentException("Teaching assignment cannot be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        this.teachingAssignment = teachingAssignment;
        this.date = date;
    }

    public AttendanceRecord registerAttendance(Enrollment enrollment, AttendanceStatus status) {
        AttendanceRecord record = new AttendanceRecord(this, enrollment, status);
        records.add(record);
        return record;
    }
}
