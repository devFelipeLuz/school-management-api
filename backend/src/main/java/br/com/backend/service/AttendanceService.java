package br.com.backend.service;

import br.com.backend.dto.request.AttendanceCreateRequestDTO;
import br.com.backend.dto.request.AttendanceRecordRequestDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AttendanceMapper;
import br.com.backend.repository.AttendanceRepository;
import br.com.backend.repository.EnrollmentRepository;
import br.com.backend.repository.TeachingAssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository repository;
    private final TeachingAssignmentRepository teachingAssignmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceService(AttendanceRepository repository,  TeachingAssignmentRepository teachingAssignmentRepository,  EnrollmentRepository enrollmentRepository) {
        this.repository = repository;
        this.teachingAssignmentRepository = teachingAssignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public AttendanceSessionResponseDTO register(AttendanceCreateRequestDTO dto) {

        TeachingAssignment assignment = teachingAssignmentRepository.findById(dto.teachingAssignmentId())
                .orElseThrow(() -> new EntityNotFoundException("Teaching Assignment Not Found"));

        AttendanceSession session = new AttendanceSession(assignment, dto.date());

        Enrollment enrollment = enrollmentRepository.findById(dto.enrollmentId())
                .orElseThrow(() -> new EntityNotFoundException("Enrollment Not Found"));

        session.registerAttendance(enrollment, dto.status());
        repository.save(session);
        return AttendanceMapper.toDTO(session);
    }

    public Page<AttendanceSessionResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AttendanceMapper::toDTO);
    }

    public AttendanceSessionResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(AttendanceMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Attendance Not Found"));
    }

    public AttendanceSessionResponseDTO update (UUID sessiontId,
                                                UUID recordId,
                                                AttendanceRecordRequestDTO recordDto) {

        AttendanceSession session = findAttendanceSession(sessiontId);
        session.updateAttendance(recordId, recordDto.status());
        return AttendanceMapper.toDTO(session);
    }

    public void delete(UUID sessionId) {
        AttendanceSession session = findAttendanceSession(sessionId);
        repository.delete(session);
    }

    private AttendanceSession findAttendanceSession(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attendance session Not Found"));
    }
}
