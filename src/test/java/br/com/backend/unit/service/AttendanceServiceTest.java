package br.com.backend.unit.service;

import br.com.backend.builders.entity.AttendanceRecordBuilder;
import br.com.backend.builders.entity.AttendanceSessionBuilder;
import br.com.backend.builders.entity.EnrollmentBuilder;
import br.com.backend.builders.entity.TeachingAssignmentBuilder;
import br.com.backend.dto.request.AttendanceSessionCreateRequest;
import br.com.backend.dto.request.AttendanceRecordUpdateRequest;
import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.AttendanceRecordRepository;
import br.com.backend.repository.AttendanceSessionRepository;
import br.com.backend.service.AttendanceService;
import br.com.backend.service.EnrollmentService;
import br.com.backend.service.TeachingAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private TeachingAssignmentService assignmentService;

    @Mock
    private AttendanceSessionRepository repository;

    @Mock
    private AttendanceRecordRepository recordRepository;

    @InjectMocks
    private AttendanceService service;

    private UUID assignmentId;
    private UUID sessionId;

    private AttendanceSessionCreateRequest createRequest;

    private TeachingAssignment assignment;
    private Enrollment enrollment;
    private AttendanceSession session;

    @BeforeEach
    void setUp() {
        assignmentId = UUID.randomUUID();
        sessionId = UUID.randomUUID();

        createRequest = new AttendanceSessionCreateRequest(assignmentId, LocalDate.now());

        assignment = TeachingAssignmentBuilder.builder().build();
        enrollment = EnrollmentBuilder.builder().build();
        session = AttendanceSessionBuilder.builder()
                .withAssignment(assignment)
                .build();
    }

    @Test
    void shouldCreateSessionAttendance() {
        when(repository.existsByTeachingAssignment_IdAndDate(
                createRequest.teachingAssignmentId(), createRequest.date()))
                .thenReturn(false);

        when(assignmentService.findAssignmentById(createRequest.teachingAssignmentId()))
                .thenReturn(assignment);

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.createSession(createRequest);

        verify(repository).save(any(AttendanceSession.class));
    }

    @Test
    void shouldUpdateAttendance() {
        AttendanceRecord attendanceRecord = AttendanceRecordBuilder.builder()
                .withSession(session)
                .withEnrollment(enrollment)
                .build();

        UUID recordId = UUID.randomUUID();
        ReflectionTestUtils.setField(attendanceRecord, "id", recordId);

        session.getRecords().add(attendanceRecord);

        AttendanceRecordUpdateRequest request =
                new AttendanceRecordUpdateRequest(AttendanceStatus.ABSENT);

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        when(recordRepository.findById(recordId))
                .thenReturn(Optional.of(attendanceRecord));

        service.updateAttendanceRecord(sessionId, recordId, request);

        verify(repository).findById(sessionId);
        assertEquals(AttendanceStatus.ABSENT, attendanceRecord.getStatus());
    }

    @Test
    void shouldDeactivateAttendance() {
        when(repository.findById(sessionId))
                .thenReturn(Optional.of(session));

        service.deactivate(sessionId);

        verify(repository).findById(sessionId);
        assertFalse(session.isActive());
    }

    @Test
    void shouldFindActiveAttendanceSessionById() {
        when(repository.findById(sessionId))
                .thenReturn(Optional.of(session));

        AttendanceSession result = service.findActiveAttendanceSessionById(sessionId);

        verify(repository).findById(sessionId);
        assertEquals(session, result);
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        when(repository.findById(sessionId))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> service.findActiveAttendanceSessionById(sessionId));
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        AttendanceRecord attendanceRecord = AttendanceRecordBuilder.builder()
                .withSession(session)
                .withEnrollment(enrollment)
                .build();

        UUID recordId = UUID.randomUUID();
        ReflectionTestUtils.setField(attendanceRecord, "id", recordId);

        session.getRecords().add(attendanceRecord);

        AttendanceRecordUpdateRequest request =
                new AttendanceRecordUpdateRequest(null);

        when(repository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(
                BusinessException.class,
                () -> service.updateAttendanceRecord(sessionId, recordId, request));
    }

    @Test
    void shouldThrowExceptionWhenSessionAlreadyExists() {
        when(repository.existsByTeachingAssignment_IdAndDate(any(), any()))
                .thenReturn(true);

        assertThrows(
                BusinessException.class,
                () -> service.createSession(createRequest));
    }
}
