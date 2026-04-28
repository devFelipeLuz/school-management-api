package br.com.backend.specification;

import br.com.backend.entity.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> nameContains(String name) {
        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            String[] terms = name.toLowerCase().split("\\s+");

            for (String term : terms) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + term + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Student> emailContains(String email) {
        return (root, query, cb) -> {

            if (email == null || email.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("email")),
                    "%" + email + "%"
            );
        };
    }

    public static Specification<Student> isActive(Boolean active) {
        return (root, query, cb) -> {

            if (active == null) {
                return null;
            }

            return cb.equal(root.get("active"), active);
        };
    }
}
