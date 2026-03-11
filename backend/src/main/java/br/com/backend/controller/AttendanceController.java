package br.com.backend.controller;

import br.com.backend.dto.request.AttendanceCreateRequestDTO;
import br.com.backend.dto.request.AttendanceRecordRequestDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @Operation(summary = "Registra Attendance")
    @PostMapping
    public AttendanceSessionResponseDTO register(@Valid @RequestBody AttendanceCreateRequestDTO dto) {
        return service.register(dto);
    }

    @Operation(summary = "Busca Attendances e retorna em páginas")
    @GetMapping
    public Page<AttendanceSessionResponseDTO> getAttendances(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Busca Attendance por ID")
    @GetMapping("/{id}")
    public AttendanceSessionResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Atualiza Attendance encontrado por ID")
    @PatchMapping("/{sessionId}/records/{recordId}")
    public AttendanceSessionResponseDTO update(@PathVariable UUID sessionId,
                                               @PathVariable UUID recordId,
                                               @Valid @RequestBody AttendanceRecordRequestDTO sessionDto) {

        return service.update(sessionId, recordId, sessionDto);
    }

    @Operation(summary = "Deleta Attendance encontrado por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
