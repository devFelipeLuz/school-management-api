package br.com.backend.specification;

import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.enums.AttendanceStatus;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecordSpecification {

    public static Specification<AttendanceRecord> withFilters(
            String studentName, String studentEmail, AttendanceStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (AttendanceRecord.class.equals(query.getResultType())) {
                root.fetch("session", JoinType.LEFT);

                Fetch<?, ?> enrollmentFetch = root.fetch("enrollment", JoinType.LEFT);
                Fetch<?, ?> studentFetch = enrollmentFetch.fetch("student", JoinType.LEFT);
                studentFetch.fetch("user", JoinType.LEFT);
            }

            List<Predicate> predicates = new ArrayList<>();

            Join<?, ?> enrollment = root.join("enrollment", JoinType.LEFT);
            Join<?, ?> student = enrollment.join("student", JoinType.LEFT);
            Join<?, ?> user = student.join("user", JoinType.LEFT);

            if (studentName != null && !studentName.isBlank()) {
                for (String term : studentName.toLowerCase().split("\\s+")) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(student.get("name")),
                                    "%" + term.toLowerCase() + "%"
                            )
                    );
                }
            }

            if (studentEmail != null && !studentEmail.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(user.get("email")),
                                "%" + studentEmail.toLowerCase() + "%"
                        )
                );
            }

            if (status != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), status)
                );
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
