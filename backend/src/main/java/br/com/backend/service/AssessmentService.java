package br.com.backend.service;

import br.com.backend.DTO.assessment.AssessmentRequestDTO;
import br.com.backend.DTO.assessment.AssessmentResponseDTO;
import br.com.backend.domain.Assessment;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.AssessmentRepository;
import br.com.backend.util.ToResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AssessmentService {

    private final AssessmentRepository repository;

    public AssessmentService(AssessmentRepository repository) {
        this.repository = repository;
    }

    public AssessmentResponseDTO register(AssessmentRequestDTO dto) {
        Assessment assessment = new Assessment(
                dto.title(),
                dto.type(),
                dto.teachingAssignment(),
                dto.studentGrade());

        repository.save(assessment);

        return ToResponseDTO.toAssessmentResponseDTO(assessment);
    }

    public Page<AssessmentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toAssessmentResponseDTO);
    }

    public AssessmentResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ToResponseDTO::toAssessmentResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Assessment não encontrado"));
    }

    public AssessmentResponseDTO update(UUID id, AssessmentRequestDTO dto) {
        Assessment assessment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment não encontrado"));

        assessment.updateData(
                dto.title(),
                dto.type(),
                dto.studentGrade()
        );

        repository.save(assessment);
        return ToResponseDTO.toAssessmentResponseDTO(assessment);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
