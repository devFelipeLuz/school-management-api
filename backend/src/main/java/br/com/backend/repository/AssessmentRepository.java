package br.com.backend.repository;

import br.com.backend.domain.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
}
