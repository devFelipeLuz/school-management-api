package br.com.backend.service;

import br.com.backend.dto.request.SchoolYearRequestDTO;
import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.entity.SchoolYear;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.SchoolYearMapper;
import br.com.backend.repository.SchoolYearRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class SchooYearService {

    private final SchoolYearRepository repository;

    public SchooYearService(SchoolYearRepository repository) {
        this.repository = repository;
    }

    public SchoolYearResponseDTO register(SchoolYearRequestDTO dto) {
        SchoolYear schoolYear = new SchoolYear(dto.year());
        repository.save(schoolYear);
        return SchoolYearMapper.toDTO(schoolYear);
    }

    public Page<SchoolYearResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(SchoolYearMapper::toDTO);
    }

    public SchoolYearResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(SchoolYearMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
    }

    public SchoolYearResponseDTO update(UUID id, SchoolYearRequestDTO dto) {
        SchoolYear schoolYear = findSchoolYear(id);
        schoolYear.updateYear(dto.year());
        return SchoolYearMapper.toDTO(schoolYear);
    }

    public void delete(UUID id) {
        SchoolYear schoolYear = findSchoolYear(id);
        schoolYear.deactivate();
    }

    private SchoolYear findSchoolYear(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SchoolYear not found"));
    }
}
