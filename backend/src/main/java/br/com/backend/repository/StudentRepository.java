package br.com.backend.repository;

import br.com.backend.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findByActiveTrue(Pageable pageable);

    Page<Student> findAll(Pageable pageable);
}
