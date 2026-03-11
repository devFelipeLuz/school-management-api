package br.com.backend.controller;

import br.com.backend.dto.request.SchoolYearRequestDTO;
import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.service.SchooYearService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/schoolyear")
public class SchoolYearController {

    private final SchooYearService service;

    public SchoolYearController(SchooYearService service) {
        this.service = service;
    }

    @Operation(summary = "Registra SchoolYear")
    @PostMapping
    public SchoolYearResponseDTO register(@Valid @RequestBody SchoolYearRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca SchoolYears e retorna em páginas")
    @GetMapping
    public Page<SchoolYearResponseDTO> getSchoolYears(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca SchoolYear por ID")
    @GetMapping("/{id}")
    public SchoolYearResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza SchoolYear encontrado por ID")
    @PatchMapping("/{id}")
    public SchoolYearResponseDTO update(@PathVariable UUID id,
                                        @Valid @RequestBody SchoolYearRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Desativa SchoolYear encontrado por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
