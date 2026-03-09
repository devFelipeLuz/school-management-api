package br.com.backend.domain;

import br.com.backend.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "attendance_record",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"attendance_session_id", "enrollment_id"})
})
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_session_id", nullable = false)
    private AttendanceSession attendanceSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    public AttendanceRecord(AttendanceSession attendanceSession, Enrollment enrollment, AttendanceStatus status) {
        if (attendanceSession == null) {
            throw new IllegalArgumentException("AttendanceSession cannot be null");
        }

        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("AttendanceStatus cannot be null");
        }

        this.attendanceSession = attendanceSession;
        this.enrollment = enrollment;
        this.status = status;
    }
}
