package br.com.backend.specification;

import br.com.backend.entity.Subject;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubjectSpecification {

    public static Specification<Subject> withName(String name) {
        return (root, query, cb) -> {

            if (name == null || name.isBlank()) return null;

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

    public static Specification<Subject> isActive(Boolean active) {
        return (root, query, cb) -> {

            if (active == null) return null;

            return cb.equal(root.get("active"), active);
        };
    }
}
