package br.com.backend.unit.entity;

import br.com.backend.builders.entity.AttendanceRecordBuilder;
import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttendanceRecordTest {
    AttendanceSession session;
    Enrollment enrollment;
    AttendanceRecord record;

    @BeforeEach
    public void setUp() {
        session = UnitHelper.getSession();

        enrollment = UnitHelper.getEnrollment();

        record = AttendanceRecordBuilder.builder()
                .withSession(session)
                .withEnrollment(enrollment)
                .build();
    }

    @Test
    public void shouldCreateAttendanceRecord() {
        assertNotNull(record);
        assertEquals(enrollment, record.getEnrollment());
    }

    @Test
    public void shouldThrowExceptionWhenCreatingRecordWithNullSession() {
        assertThrows(NullPointerException.class, () -> new AttendanceRecord(null, enrollment, AttendanceStatus.PRESENT));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingRecordWithNullEnrollment() {
        assertThrows(NullPointerException.class, () -> new AttendanceRecord(session, null, AttendanceStatus.PRESENT));
    }

    @Test
    public void shouldThrowExceptionWhenCreatingRecordWithInvalidAttendanceStatus() {
        assertThrows(BusinessException.class, () -> new AttendanceRecord(session, enrollment, null));
    }

    @Test
    public void shouldUpdateStatus() {
        record.updateStatus(AttendanceStatus.ABSENT);
        assertEquals(AttendanceStatus.ABSENT, record.getStatus());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingStatusWithInvalidAttendanceStatus() {
        assertThrows(BusinessException.class, () -> record.updateStatus(null));
    }
}
