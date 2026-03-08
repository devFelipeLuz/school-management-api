package br.com.backend.controller;

import br.com.backend.DTO.grade.GradeRequestDTO;
import br.com.backend.DTO.grade.GradeResponseDTO;
import br.com.backend.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
    public Page<GradeResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public GradeResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
