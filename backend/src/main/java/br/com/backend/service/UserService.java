package br.com.backend.service;

import br.com.backend.DTO.request.UserCreateRequestDTO;
import br.com.backend.DTO.response.UserResponseDTO;
import br.com.backend.entity.User;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.mapper.UserMapper;
import br.com.backend.repository.UserRepository;
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

    public UserResponseDTO create(UserCreateRequestDTO dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        User adminUser = User.createUser(
                dto.getEmail(),
                encodedPassword,
                dto.getRole());

        repository.save(adminUser);

        return UserMapper.toDTO(adminUser);
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(UserMapper::toDTO);
    }

    public Page<UserResponseDTO> findAllEnabled(Pageable pageable) {
        return repository.findAllByEnabledTrue(pageable)
                .map(UserMapper::toDTO);
    }

    public UserResponseDTO findById(UUID id) {
        User user = findActiveUserById(id);
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO updateEmail(UUID id, UserCreateRequestDTO dto) {
        User adminUser = findActiveUserById(id);
        adminUser.updateEmail(dto.getEmail());
        return UserMapper.toDTO(adminUser);
    }

    public UserResponseDTO updatePassword(UUID id, UserCreateRequestDTO dto) {
        String encondedPassword = encoder.encode(dto.getPassword());
        User adminUser = findActiveUserById(id);
        adminUser.updatePassword(encondedPassword);
        return UserMapper.toDTO(adminUser);
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