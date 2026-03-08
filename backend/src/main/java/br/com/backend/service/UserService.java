package br.com.backend.service;

import br.com.backend.DTO.user.AdminUserCreateRequestDTO;
import br.com.backend.DTO.user.PublicUserCreateRequestDTO;
import br.com.backend.DTO.user.UserResponseDTO;
import br.com.backend.domain.enums.Role;
import br.com.backend.domain.User;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.UserRepository;
import br.com.backend.util.ToResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public UserResponseDTO createAdminUser(AdminUserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        User adminUser = User.createAdminUser(
                dto.getUsername(),
                encodedPassword,
                Role.ADMIN);

        repository.save(adminUser);

        return ToResponseDTO.toUserResponseDTO(adminUser);
    }

    public UserResponseDTO createPublicUser(PublicUserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        User user = User.createGlobalUser(
                dto.getUsername(),
                encodedPassword);

        repository.save(user);

        return ToResponseDTO.toUserResponseDTO(user);
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ToResponseDTO::toUserResponseDTO);
    }

    public Page<UserResponseDTO> findAllEnabled(Pageable pageable) {
        return repository.findAllByEnabledTrue(pageable)
                .map(ToResponseDTO::toUserResponseDTO);
    }

    public UserResponseDTO findById(UUID id) {
        User user = findActiveUserById(id);
        return ToResponseDTO.toUserResponseDTO(user);
    }

    public UserResponseDTO updateAdminUsername(UUID id, AdminUserCreateRequestDTO dto) {
        User adminUser = findActiveUserById(id);
        adminUser.updateUsername(dto.getUsername());
        return ToResponseDTO.toUserResponseDTO(adminUser);
    }

    public UserResponseDTO updateAdminUserPassword(UUID id, AdminUserCreateRequestDTO dto) {
        String encondedPassword = encoder.encode(dto.getPassword());
        User adminUser = findActiveUserById(id);
        adminUser.updatePassword(encondedPassword);
        return ToResponseDTO.toUserResponseDTO(adminUser);
    }

    public UserResponseDTO updatePublicUserUsername(UUID id, PublicUserCreateRequestDTO dto) {
        User user = findActiveUserById(id);
        user.updateUsername(dto.getUsername());
        return ToResponseDTO.toUserResponseDTO(user);
    }

    public UserResponseDTO updatePublicUserPassword(UUID id, PublicUserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());
        User user = findActiveUserById(id);
        user.updatePassword(encodedPassword);
        return ToResponseDTO.toUserResponseDTO(user);
    }

    public void delete(UUID id) {
        User user = findActiveUserById(id);
        user.deactivate();
    }

    public User findActiveUserById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User não encontrado"));
        user.isEnabled();
        return user;
    }
}