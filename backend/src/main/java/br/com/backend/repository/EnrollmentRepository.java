package br.com.backend.repository;

import br.com.backend.entity.Enrollment;
import br.com.backend.entity.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    Page<Enrollment> findAll(Pageable pageable);
}
