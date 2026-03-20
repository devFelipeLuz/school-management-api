package br.com.backend.repository;

import br.com.backend.entity.SchoolYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, UUID> {
    Page<SchoolYear> findAll(Pageable pageable);

    Page<SchoolYear> findByActive(Boolean active, Pageable pageable);
}
