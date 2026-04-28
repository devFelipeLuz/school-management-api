package br.com.backend.service;

import br.com.backend.dto.request.SubjectCreateRequest;
import br.com.backend.dto.request.SubjectUpdateRequest;
import br.com.backend.dto.response.SubjectResponseDTO;
import br.com.backend.entity.Subject;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.SubjectMapper;
import br.com.backend.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.backend.specification.SubjectSpecification.*;

import java.util.UUID;

@Service
@Transactional
public class SubjectService {

    private final SubjectRepository repository;

    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public SubjectResponseDTO register(SubjectCreateRequest dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Subject already exists");
        }

        Subject subject = new Subject(dto.name());
        Subject saved = repository.save(subject);
        return SubjectMapper.toDTO(saved);
    }

    public SubjectResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(SubjectMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
    }

    public Page<SubjectResponseDTO> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(SubjectMapper::toDTO);
    }

    public Page<SubjectResponseDTO> findAll(String subjectName, Boolean active, Pageable pageable) {
        Specification<Subject> spec = Specification
                .where(withName(subjectName)).
                and(isActive(active));

        return repository.findAll(spec, pageable)
                .map(SubjectMapper::toDTO);
    }

    public SubjectResponseDTO update(UUID id, SubjectUpdateRequest dto) {
        if (repository.existsByNameIgnoreCase(dto.name())) {
            throw new BusinessException("Subject already exists");
        }

        Subject subject = findActiveSubjectById(id);

        if (dto.name() != null) {
            subject.updateName(dto.name());
        }

        return SubjectMapper.toDTO(subject);
    }

    public SubjectResponseDTO activate(UUID id) {
        Subject subject = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
        subject.activate();
        return SubjectMapper.toDTO(subject);
    }

    public void deactivate(UUID id) {
        Subject subject = findActiveSubjectById(id);
        subject.deactivate();
    }

    public Subject findActiveSubjectById(UUID id) {
        Subject subject = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found"));
        subject.ensureActive();
        return subject;
    }
}
