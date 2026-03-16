package br.com.backend.controller;

import br.com.backend.dto.request.UpdatePasswordRequest;
import br.com.backend.dto.request.UpdateUsernameRequest;
import br.com.backend.dto.request.UserCreateRequest;
import br.com.backend.dto.response.UserResponseDTO;
import br.com.backend.service.UserService;
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
@RequestMapping("/admin/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Create user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody UserCreateRequest dto) {
        return service.register(dto);
    }

    @Operation(summary = "List users")
    @GetMapping
    public Page<UserResponseDTO> getUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "List enabled users")
    @GetMapping("/enabled")
    public Page<UserResponseDTO> getEnabledUsers(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findAllEnabled(pageable);
    }

    @Operation(summary = "Find user by id")
    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update username")
    @PatchMapping("/{id}/username")
    public UserResponseDTO updateUsername(@PathVariable UUID id,
                                          @Valid @RequestBody UpdateUsernameRequest dto) {
        return service.updateEmail(id, dto);
    }

    @Operation(summary = "Update password")
    @PatchMapping("/{id}/password")
    public UserResponseDTO updatePassword(@PathVariable UUID id,
                                          @Valid @RequestBody UpdatePasswordRequest dto){
        return service.updatePassword(id, dto);
    }

    @Operation(summary = "Deactivate user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deactivateUser(@PathVariable UUID id) {
        service.deactivate(id);
    }
}
