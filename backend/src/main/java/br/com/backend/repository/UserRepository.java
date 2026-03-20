package br.com.backend.repository;

import br.com.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Page<User> findAll(Pageable pageable);

    Page<User> findByEnabled(Boolean enabled, Pageable pageable);

    Optional<User> findByEmail(String email);
}
