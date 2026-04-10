package br.com.backend.entity;

import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

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

    @Column(nullable = false)
    private boolean active;

    public AttendanceSession(TeachingAssignment teachingAssignment, LocalDate date) {
        this.teachingAssignment = Objects.requireNonNull(
                teachingAssignment, "Teaching assignment cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.active = true;
    }

    public void registerAttendance(Enrollment enrollment, AttendanceStatus status) {
        ensureActive();
        ensureEnrollmentBelongsToAssignment(enrollment);

        if (isAttendanceIsAlreadyRegistered(enrollment)) {
            throw new BusinessException("Attendance already registered for this student");
        }

        AttendanceRecord attendanceRecord =
                new AttendanceRecord(this, enrollment, status);

        records.add(attendanceRecord);
    }

    private void ensureActive() {
        if (!this.active) {
            throw new BusinessException("Attendance is not active");
        }
    }

    private void ensureEnrollmentBelongsToAssignment(Enrollment enrollment) {
        if (!enrollment.getClassroom().equals(teachingAssignment.getClassroom())) {
            throw new BusinessException("Enrollment does not belong to this class");
        }
    }

    private boolean isAttendanceIsAlreadyRegistered(Enrollment enrollment) {
        return this.records.stream()
                .anyMatch(r -> r.getEnrollment().equals(enrollment));
    }
}
