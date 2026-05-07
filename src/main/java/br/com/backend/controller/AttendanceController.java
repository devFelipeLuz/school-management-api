package br.com.backend.controller;

import br.com.backend.dto.request.AttendanceRecordCreateRequest;
import br.com.backend.dto.request.AttendanceSessionCreateRequest;
import br.com.backend.dto.request.AttendanceRecordUpdateRequest;
import br.com.backend.dto.request.AttendanceSessionFilter;
import br.com.backend.dto.response.AttendanceRecordResponseDTO;
import br.com.backend.dto.response.AttendanceSessionResponseDTO;
import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/attendance-sessions")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @Operation(summary = "Create attendance")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AttendanceSessionResponseDTO registerAttendance(@Valid @RequestBody AttendanceSessionCreateRequest dto) {
        return service.createSession(dto);
    }

    @Operation(summary = "Register attendance record")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{sessionId}/records")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public void registerRecord(
            @PathVariable UUID sessionId,

            @Valid @RequestBody AttendanceRecordCreateRequest request) {

        service.registerRecord(sessionId, request);
    }

    @Operation(summary = "Find attendance by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AttendanceSessionResponseDTO getAttendanceById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "List sessions")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<AttendanceSessionResponseDTO> geAttendances(
            @RequestParam(required = false) AttendanceSessionFilter filter,

            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.listSessions(filter, pageable);
    }

    @Operation(summary = "List records")
    @GetMapping("/{sessionId}/records")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public Page<AttendanceRecordResponseDTO> geAttendances(
            @PathVariable UUID sessionId,

            @Parameter(description = "Filter by student's partial or full name")
            @RequestParam(required = false) String studentName,

            @Parameter(description = "Filter by status PRESENT, ABSENT or JUSTIFIED_ABSENCE")
            @RequestParam(required = false) AttendanceStatus status,

            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.listRecords(sessionId, studentName, status, pageable);
    }

    @Operation(summary = "Update attendance")
    @PatchMapping("/{sessionId}/records/{recordId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public AttendanceRecordResponseDTO updateAttendance(
            @PathVariable UUID sessionId,
            @PathVariable UUID recordId,
            @Valid @RequestBody AttendanceRecordUpdateRequest request) {

        return service.updateAttendanceRecord(sessionId, recordId, request);
    }

    @Operation(summary = "Delete attendance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESSOR')")
    public void deleteAttendance(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
