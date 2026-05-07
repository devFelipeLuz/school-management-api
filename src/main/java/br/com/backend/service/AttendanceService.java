package br.com.backend.service;

import br.com.backend.dto.request.AttendanceRecordCreateRequest;
import br.com.backend.dto.request.AttendanceSessionCreateRequest;
import br.com.backend.dto.request.AttendanceRecordUpdateRequest;
import br.com.backend.dto.request.AttendanceSessionFilter;
import br.com.backend.dto.response.AttendanceRecordResponseDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AttendanceRecordMapper;
import br.com.backend.mapper.AttendanceSessionMapper;
import br.com.backend.repository.AttendanceRecordRepository;
import br.com.backend.repository.AttendanceSessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.backend.specification.AttendanceSessionSpecification.*;
import static br.com.backend.specification.AttendanceRecordSpecification.*;

import java.util.UUID;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final TeachingAssignmentService teachingAssignmentService;
    private final EnrollmentService enrollmentService;

    public AttendanceService(AttendanceSessionRepository sessionRepository,
                             AttendanceRecordRepository recordRepository,
                             TeachingAssignmentService teachingAssignmentService,
                             EnrollmentService enrollmentService) {

        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.teachingAssignmentService = teachingAssignmentService;
        this.enrollmentService = enrollmentService;
    }

    public AttendanceSessionResponseDTO createSession(AttendanceSessionCreateRequest dto) {

        if (sessionRepository.existsByTeachingAssignment_IdAndDate(dto.teachingAssignmentId(), dto.date())) {
            throw new BusinessException("Session already exists");
        }

        TeachingAssignment assignment = teachingAssignmentService
                .findAssignmentById(dto.teachingAssignmentId());

        AttendanceSession session = new AttendanceSession(assignment, dto.date());

        AttendanceSession saved = sessionRepository.save(session);
        return AttendanceSessionMapper.toDTO(saved);
    }

    public void registerRecord(UUID sessionId, AttendanceRecordCreateRequest request) {
        AttendanceSession session = findActiveAttendanceSessionById(sessionId);

        session.registerAttendance(
                enrollmentService.findActiveEnrollmentById(request.enrollmentId()),
                request.status());
    }

    public Page<AttendanceSessionResponseDTO> listSessions(AttendanceSessionFilter filter, Pageable pageable) {
        Specification<AttendanceSession> spec = Specification
                .where(withProfessor(filter.professorName()))
                .and(withSubject(filter.subject()))
                .and(withClassroom(filter.classroomName()))
                .and(withDate(filter.date()));

        return sessionRepository.findAll(spec, pageable)
                .map(AttendanceSessionMapper::toDTO);
    }

    public Page<AttendanceRecordResponseDTO> listRecords(UUID sessionId, String studentName, AttendanceStatus status, Pageable pageable) {
        Specification<AttendanceRecord> spec = Specification
                .where(withSessionId(sessionId))
                .and(withStudentName(studentName))
                .and(withStatus(status));

        return recordRepository.findAll(spec, pageable)
                .map(AttendanceRecordMapper::toDTO);
    }

    public AttendanceSessionResponseDTO findById(UUID id) {
        return sessionRepository.findById(id)
                .map(AttendanceSessionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Attendance Not Found"));
    }

    public AttendanceRecordResponseDTO updateAttendanceRecord(UUID sessionId, UUID recordId, AttendanceRecordUpdateRequest request) {
        AttendanceSession session = findActiveAttendanceSessionById(sessionId);
        session.updateAttendance(recordId, request.status());

        return AttendanceRecordMapper.toDTO(findAttendanceRecordById(recordId));
    }

    public void deactivate(UUID sessionId) {
        AttendanceSession session = findActiveAttendanceSessionById(sessionId);
        session.deactivate();
    }

    public AttendanceSession findActiveAttendanceSessionById(UUID id) {
        AttendanceSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendance session Not Found"));
        session.ensureActive();
        return session;
    }

    private AttendanceRecord findAttendanceRecordById(UUID id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendance Record not found"));
    }
}
