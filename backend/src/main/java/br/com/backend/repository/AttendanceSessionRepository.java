package br.com.backend.repository;

import br.com.backend.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.UUID;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, UUID>,
        JpaSpecificationExecutor<AttendanceSession> {

    boolean existsByTeachingAssignmentAndDate(UUID assignmentId, LocalDate date);
}
