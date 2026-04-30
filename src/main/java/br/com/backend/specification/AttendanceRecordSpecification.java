package br.com.backend.specification;

import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.Student;
import br.com.backend.entity.User;
import br.com.backend.entity.enums.AttendanceStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecordSpecification {

    public static Specification<AttendanceRecord> withStudentName(String name) {
        return (root, query, cb) -> {

            if (AttendanceRecord.class.equals(query.getResultType())) {
                root.fetch("enrollment", JoinType.LEFT)
                        .fetch("student", JoinType.LEFT);
            }

            if (name == null || name.isBlank()) {
                return null;
            }

            Join<Enrollment, Student> studentJoin = root
                    .join("enrollment", JoinType.LEFT)
                    .join("student", JoinType.LEFT);


            return buildLikePredicate(cb, studentJoin.get("name"), name);
        };
    }

    public static Specification<AttendanceRecord> withStudentEmail(String email) {
        return (root, query, cb) -> {

            if (AttendanceRecord.class.equals(query.getResultType())) {
                root.fetch("enrollment", JoinType.LEFT)
                        .fetch("student", JoinType.LEFT)
                        .fetch("email", JoinType.LEFT);
            }

            if (email == null || email.isBlank()) {
                return null;
            }

            Join<Student, User> user = root
                    .join("enrollment", JoinType.LEFT).
                    join("student", JoinType.LEFT).
                    join("user", JoinType.LEFT);

            return buildLikePredicate(cb, user.get("email"), email);
        };
    }

    public static Specification<AttendanceRecord> withStatus(AttendanceStatus status) {
        return (root, query, cb) -> {

            if (status == null) {
                return null;
            }

            return cb.equal(root.get("status"), status);
        };
    }

    private static Predicate buildLikePredicate(CriteriaBuilder cb, Path<String> field, String value) {
        List<Predicate> predicates = new ArrayList<>();

        String[] terms = value.toLowerCase().split("\\s+");

        for (String term : terms) {
            predicates.add(
                    cb.like(
                            cb.lower(field), "%" + term + "%")
            );
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }


}
