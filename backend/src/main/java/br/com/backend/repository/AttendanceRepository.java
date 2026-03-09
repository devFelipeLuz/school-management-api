package br.com.backend.repository;

import br.com.backend.domain.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<AttendanceSession, UUID> {
}
