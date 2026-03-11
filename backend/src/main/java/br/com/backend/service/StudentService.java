package br.com.backend.service;

import br.com.backend.DTO.request.StudentRequestDTO;
import br.com.backend.DTO.response.StudentResponseDTO;
import br.com.backend.entity.Student;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.StudentMapper;
import br.com.backend.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponseDTO create(StudentRequestDTO dto) {
        Student student = new Student(dto.getName());
        repository.save(student);
        return StudentMapper.toDTO(student);
    }

    public Page<StudentResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(StudentMapper::toDTO);
    }

    public Page<StudentResponseDTO> findAllByActive(Pageable pageable) {
        return repository.findByActiveTrue(pageable)
                .map(StudentMapper::toDTO);
    }

    public StudentResponseDTO findById(UUID id) {
        Student student = findActiveStudentById(id);
        return StudentMapper.toDTO(student);
    }

    public StudentResponseDTO update(UUID id, StudentRequestDTO dto) {
        Student student = findActiveStudentById(id);
        student.updateData(dto.getName());
        repository.save(student);
        return StudentMapper.toDTO(student);
    }

    public void delete(UUID id) {
        Student student = findActiveStudentById(id);
        student.deactivate();
        repository.save(student);
    }

    private Student findActiveStudentById(UUID id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        if (!student.isActive()) {
            throw new BusinessException("Aluno inativo");
        }

        return student;
    }
}
