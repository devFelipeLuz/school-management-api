package br.com.backend.service;

import br.com.backend.DTO.classroom.ClassroomRequestDTO;
import br.com.backend.DTO.classroom.ClassroomResponseDTO;
import br.com.backend.domain.Classroom;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.ClassroomRepository;
import br.com.backend.util.ToResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClassroomService {

    private ClassroomRepository repository;

    public ClassroomService(ClassroomRepository repository) {
        this.repository = repository;
    }

    public ClassroomResponseDTO create(ClassroomRequestDTO dto) {
        Classroom classroom = new Classroom(dto.getName(), dto.getSchoolYear());
        repository.save(classroom);
        return ToResponseDTO.toClassroomResponseDTO(classroom);
    }

    public Page<ClassroomResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toClassroomResponseDTO);
    }

    public ClassroomResponseDTO findById(UUID id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom não encontrada"));

        return ToResponseDTO.toClassroomResponseDTO(classroom);
    }
}
