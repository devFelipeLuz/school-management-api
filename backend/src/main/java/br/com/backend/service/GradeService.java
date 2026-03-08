package br.com.backend.service;

import br.com.backend.DTO.grade.GradeRequestDTO;
import br.com.backend.DTO.grade.GradeResponseDTO;
import br.com.backend.domain.Grade;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.GradeRepository;
import br.com.backend.util.ToResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GradeService {

    private GradeRepository repository;

    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public GradeResponseDTO create(GradeRequestDTO dto) {
        Grade grade = new Grade(
                dto.getName()
        );
        repository.save(grade);
        return ToResponseDTO.toGradeResponseDTO(grade);
    }

    public Page<GradeResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toGradeResponseDTO);
    }

    public GradeResponseDTO findById(UUID id) {
        Grade grade = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada"));

        return ToResponseDTO.toGradeResponseDTO(grade);
    }
}
