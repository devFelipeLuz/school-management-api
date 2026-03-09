package br.com.backend.repository;

import br.com.backend.domain.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, UUID> {
}
