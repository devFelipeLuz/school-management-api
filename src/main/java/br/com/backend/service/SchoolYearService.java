package br.com.backend.service;

import br.com.backend.dto.request.SchoolYearRequest;
import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.SchoolYearMapper;
import br.com.backend.repository.SchoolYearRepository;
import br.com.backend.specification.GenericSpecification;
import br.com.backend.specification.SchoolYearSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class SchoolYearService {

    private final SchoolYearRepository repository;

    public SchoolYearService(SchoolYearRepository repository) {
        this.repository = repository;
    }

    public SchoolYearResponseDTO register(SchoolYearRequest dto) {
        int currentYear = Year.now().getValue();

        if (dto.year() < currentYear) {
            throw new BusinessException("Cannot create a school year in the past.");
        }

        if (repository.existsByYear(dto.year())) {

            throw new BusinessException("SchoolYear already exists");
        }

        SchoolYear schoolYear = new SchoolYear(dto.year());
        SchoolYear saved = repository.save(schoolYear);
        return SchoolYearMapper.toDTO(saved);
    }

    public SchoolYearResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(SchoolYearMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
    }

    public Page<SchoolYearResponseDTO> findAll(String year, Boolean active, Pageable pageable) {
        Specification<SchoolYear> spec =
                SchoolYearSpecification.withFilters(year, active);

        return repository.findAll(spec, pageable)
                .map(SchoolYearMapper::toDTO);
    }

    public SchoolYearResponseDTO update(UUID id, SchoolYearRequest dto) {
        if (repository.existsByYear(dto.year())) {

            throw new BusinessException("SchoolYear already exists");
        }

        SchoolYear schoolYear = findActiveSchoolYear(id);
        schoolYear.updateYear(dto.year());
        return SchoolYearMapper.toDTO(schoolYear);
    }

    public SchoolYearResponseDTO activate(UUID id) {
        SchoolYear schoolYear = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
        schoolYear.activate();
        return SchoolYearMapper.toDTO(schoolYear);
    }

    public void deactivate(UUID id) {
        SchoolYear schoolYear = findActiveSchoolYear(id);
        schoolYear.deactivate();
    }

    public SchoolYear findActiveSchoolYear(UUID id) {
        SchoolYear schoolYear = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
        schoolYear.ensureActive();
        return schoolYear;
    }
}
