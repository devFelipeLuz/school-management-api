package br.com.backend.service;

import br.com.backend.dto.request.AssessmentCreateRequest;
import br.com.backend.dto.request.AssessmentUpdateRequest;
import br.com.backend.dto.response.AssessmentResponseDTO;
import br.com.backend.entity.Assessment;
import br.com.backend.entity.TeachingAssignment;
import br.com.backend.entity.enums.AssessmentType;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.AssessmentMapper;
import br.com.backend.repository.AssessmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.backend.specification.AssessmentSpecification.*;

import java.util.UUID;

@Service
@Transactional
public class AssessmentService {

    private final AssessmentRepository repository;
    private final TeachingAssignmentService teachingAssignmentService;

    public AssessmentService(AssessmentRepository repository,
                             TeachingAssignmentService teachingAssignmentService) {

        this.repository = repository;
        this.teachingAssignmentService = teachingAssignmentService;
    }

    public AssessmentResponseDTO register(AssessmentCreateRequest dto) {
        TeachingAssignment assignment = teachingAssignmentService
                .findAssignmentById(dto.teachingAssignmentId());

        assignment.ensureAllIsActive();

        Assessment assessment = new Assessment(assignment, dto.title(), dto.type());

        Assessment saved = repository.save(assessment);

        return AssessmentMapper.toDTO(saved);
    }

    public AssessmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(AssessmentMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
    }

    public Page<AssessmentResponseDTO> findAll(String title, AssessmentType type, Boolean active, Pageable pageable) {
        Specification<Assessment> spec = Specification
                .where(titleContains(title))
                .and(withType(type))
                .and(isActive(active));

        return repository.findAll(spec, pageable)
                .map(AssessmentMapper::toDTO);
    }

    public AssessmentResponseDTO update(UUID id, AssessmentUpdateRequest dto) {
        Assessment assessment = findActiveAssessmentById(id);

        if (ensureStringIsNotNull(dto.title())) {
            assessment.updateTitle(dto.title());
        }

        if (ensureAssessmentTypeIsNotNull(dto.type())) {
            assessment.updateType(dto.type());
        }

        return AssessmentMapper.toDTO(assessment);
    }

    public AssessmentResponseDTO activate(UUID id) {
        Assessment assessment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        assessment.activate();
        return AssessmentMapper.toDTO(assessment);
    }

    public void deactivate(UUID id) {
        Assessment assessment = findActiveAssessmentById(id);
        assessment.deactivate();
    }

    public Assessment findActiveAssessmentById(UUID assessmentId) {
        Assessment assessment = repository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        assessment.ensureActive();
        return assessment;
    }

    private boolean ensureStringIsNotNull(String value) {
        return value != null;
    }

    private boolean ensureAssessmentTypeIsNotNull(AssessmentType type) {
        return type != null;
    }
}
