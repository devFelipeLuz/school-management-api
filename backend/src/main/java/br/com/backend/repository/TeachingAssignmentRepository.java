package br.com.backend.repository;

import br.com.backend.entity.TeachingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, UUID> {
}
