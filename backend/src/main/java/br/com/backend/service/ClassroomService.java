package br.com.backend.service;

import br.com.backend.DTO.request.ClassroomRequestDTO;
import br.com.backend.DTO.response.ClassroomResponseDTO;
import br.com.backend.entity.Classroom;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.ClassroomMapper;
import br.com.backend.repository.ClassroomRepository;
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
        return ClassroomMapper.toDTO(classroom);
    }

    public Page<ClassroomResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ClassroomMapper::toDTO);
    }

    public ClassroomResponseDTO findById(UUID id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom não encontrada"));

        return ClassroomMapper.toDTO(classroom);
    }
}
