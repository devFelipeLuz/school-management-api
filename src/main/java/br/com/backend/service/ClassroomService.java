package br.com.backend.service;

import br.com.backend.dto.request.ClassroomUpdateRequest;
import br.com.backend.dto.request.ClassroomCreateRequest;
import br.com.backend.dto.response.ClassroomResponseDTO;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.ClassroomMapper;
import br.com.backend.repository.ClassroomRepository;
import br.com.backend.specification.ClassroomSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ClassroomService {

    private final ClassroomRepository repository;
    private final SchoolYearService schoolYearService;

    public ClassroomService(
            ClassroomRepository repository, SchoolYearService schoolYearService) {

        this.repository = repository;
        this.schoolYearService = schoolYearService;
    }

    public ClassroomResponseDTO register(ClassroomCreateRequest dto) {
        SchoolYear schoolYear = schoolYearService.findActiveSchoolYear(dto.schoolYearId());

        Classroom classroom = new Classroom(dto.name(), schoolYear);
        Classroom saved = repository.save(classroom);
        return ClassroomMapper.toDTO(saved);
    }

    public Page<ClassroomResponseDTO> findAll(String name, Boolean active, Pageable pageable) {
        Specification<Classroom> spec =
                ClassroomSpecification.withFilters(name, active);

        return repository.findAll(spec, pageable)
                .map(ClassroomMapper::toDTO);
    }

    public ClassroomResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(ClassroomMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
    }

    public ClassroomResponseDTO update(UUID id, ClassroomUpdateRequest dto) {
        Classroom classroom = findActiveClassroomById(id);

        if (dto.name() != null && !dto.name().isBlank()) {
            classroom.updateName(dto.name());
        }

        if (dto.newCapacity() != null) {
            classroom.changeCapacity(dto.newCapacity());
        }

        return ClassroomMapper.toDTO(classroom);
    }

    public ClassroomResponseDTO activate(UUID id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        classroom.activate();
        return ClassroomMapper.toDTO(classroom);
    }

    public void deactivate(UUID id) {
        Classroom classroom = findActiveClassroomById(id);
        classroom.deactivate();
    }

    public Classroom findActiveClassroomById(UUID id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom Not Found"));
        classroom.ensureActive();
        return classroom;
    }
}
