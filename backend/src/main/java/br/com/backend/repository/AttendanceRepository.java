package br.com.backend.repository;

import br.com.backend.entity.AttendanceSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<AttendanceSession, UUID> {
    Page<AttendanceSession> findAll(Pageable pageable);
}
