package br.com.backend.repository;

import br.com.backend.entity.TeachingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, UUID>, JpaSpecificationExecutor<TeachingAssignment> {

    boolean existsByProfessorIdAndSubjectIdAndClassroomId(
            UUID professorId, UUID subjectId, UUID classroomId);
}
