package br.com.backend.specification;

import br.com.backend.entity.Enrollment;
import br.com.backend.entity.Student;
import br.com.backend.entity.enums.EnrollmentStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentSpecification {
    
    public static Specification<Enrollment> withStudentName(String studentName) {
        return (root, query, cb) -> {
            if (Enrollment.class.equals(query.getResultType())) {
                root.fetch("student", JoinType.LEFT);
                query.distinct(true);
            }

            if (studentName == null || studentName.isBlank()) {
                return null;
            }

            Join<Enrollment, Student> student = root.join("student", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            String[] terms = studentName.toLowerCase().split("\\s+");

            for (String term : terms) {
                predicates.add(
                        cb.like(
                                cb.lower(student.get("name")),
                                "%" + term.toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Enrollment> withStatus(EnrollmentStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            return cb.equal(root.get("status"), status);
        };
    }
}
