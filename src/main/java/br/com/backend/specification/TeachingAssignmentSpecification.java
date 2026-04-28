package br.com.backend.specification;

import br.com.backend.entity.Classroom;
import br.com.backend.entity.Professor;
import br.com.backend.entity.Subject;
import br.com.backend.entity.TeachingAssignment;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TeachingAssignmentSpecification {

    public static Specification<TeachingAssignment> withProfessor(String professorName) {
        return (root, query, cb) -> {

            if (TeachingAssignment.class.equals(query.getResultType())) {
                root.fetch("professor", JoinType.LEFT);
                query.distinct(true);
            }

            if (professorName == null || professorName.isBlank()) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            Join<TeachingAssignment, Professor> professor = root.join("professor", JoinType.LEFT);

            return buildLikePredicate(cb, professor.get("name"), professorName);
        };
    }

    public static Specification<TeachingAssignment> withSubject(String subjectName) {
        return (root, query, cb) -> {

            if (TeachingAssignment.class.equals(query.getResultType())) {
                root.fetch("subject", JoinType.LEFT);
                query.distinct(true);
            }

            if (subjectName == null || subjectName.isBlank()) {
                return null;
            }

            Join<TeachingAssignment, Subject> subject = root.join("subject", JoinType.LEFT);

            return buildLikePredicate(cb, subject.get("name"), subjectName);
        };
    }

    public static Specification<TeachingAssignment> withClassroom(String classroomName) {
        return (root, query, cb) -> {

            if (TeachingAssignment.class.equals(query.getResultType())) {
                root.fetch("classroom", JoinType.LEFT);
                query.distinct(true);
            }

            if (classroomName == null || classroomName.isBlank()) {
                return null;
            }

            Join<TeachingAssignment, Classroom> classroom = root.join("classroom", JoinType.LEFT);

            return buildLikePredicate(cb, classroom.get("name"), classroomName);
        };
    }

    private static Predicate buildLikePredicate(
            CriteriaBuilder cb,
            Path<String> field,
            String value
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (value != null && !value.isBlank()) {
            String[] terms = value.toLowerCase().split("\\s+");

            for (String term : terms) {
                predicates.add(
                        cb.like(
                                cb.lower(field),
                                "%" + term + "%"
                        )
                );
            }
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
