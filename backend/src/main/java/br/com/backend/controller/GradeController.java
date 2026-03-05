package br.com.backend.controller;

import br.com.backend.DTO.grade.GradeRequestDTO;
import br.com.backend.DTO.grade.GradeResponseDTO;
import br.com.backend.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeService service;

    public GradeController(GradeService service) {
        this.service = service;
    }

    @PostMapping
    public GradeResponseDTO register(@Valid @RequestBody GradeRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<GradeResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public GradeResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
