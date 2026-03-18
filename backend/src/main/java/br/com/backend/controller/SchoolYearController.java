package br.com.backend.controller;

import br.com.backend.dto.request.SchoolYearRequest;
import br.com.backend.dto.response.SchoolYearResponseDTO;
import br.com.backend.service.SchoolYearService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/school-years")
public class SchoolYearController {

    private final SchoolYearService service;

    public SchoolYearController(SchoolYearService service) {
        this.service = service;
    }

    @Operation(summary = "Create schoolYear")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SchoolYearResponseDTO register(@Valid @RequestBody SchoolYearRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List schoolYears")
    @GetMapping
    public Page<SchoolYearResponseDTO> getSchoolYears(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Find schoolYear by id")
    @GetMapping("/{id}")
    public SchoolYearResponseDTO getSchollYearById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update schoolYear")
    @PatchMapping("/{id}")
    public SchoolYearResponseDTO update(@PathVariable UUID id,
                                        @Valid @RequestBody SchoolYearRequest dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Deactivate schoolYear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deactivateSchoolYear(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
