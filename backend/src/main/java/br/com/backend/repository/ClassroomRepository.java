package br.com.backend.repository;

import br.com.backend.domain.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, UUID> {
}
