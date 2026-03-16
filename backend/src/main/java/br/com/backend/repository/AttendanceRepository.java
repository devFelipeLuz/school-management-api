package br.com.backend.repository;

import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.TeachingAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<AttendanceSession, UUID> {
    Page<AttendanceSession> findAll(Pageable pageable);

    boolean existsByAssignmentAndDate(UUID assignmentId, LocalDate date);
}
