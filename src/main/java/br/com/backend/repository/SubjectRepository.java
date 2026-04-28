package br.com.backend.repository;

import br.com.backend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID>, JpaSpecificationExecutor<Subject> {
    boolean existsByNameIgnoreCase(String name);

    long countByActiveTrue();

    Page<Subject> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
