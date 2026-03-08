package br.com.backend.controller;

import br.com.backend.DTO.user.AdminUserCreateRequestDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDTO register(@Valid @RequestBody AdminUserCreateRequestDTO dto) {
        return service.createAdminUser(dto);
    }

    @GetMapping
    public Page<UserResponseDTO> findAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping
    public Page<UserResponseDTO> findAllEnabled(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return service.findAllEnabled(pageable);
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateAdminUserUsername(@PathVariable UUID id,
                                                   @Valid @RequestBody AdminUserCreateRequestDTO dto) {
        return service.updateAdminUsername(id, dto);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateAdminUserPassword(@PathVariable UUID id,
                                                   @Valid @RequestBody AdminUserCreateRequestDTO dto){
        return service.updateAdminUserPassword(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
