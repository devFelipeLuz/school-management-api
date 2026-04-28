package br.com.backend.specification;

import br.com.backend.entity.Assessment;
import br.com.backend.entity.enums.AssessmentType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AssessmentSpecification {
    
    public static Specification<Assessment> titleContains(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            String[] terms = title.toLowerCase().split("\\s+");

            for (String term : terms) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + term + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Assessment> withType(AssessmentType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return null;
            }

            return cb.equal(root.get("type"), type);
        };
    }

    public static Specification<Assessment> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }

            return cb.equal(root.get("active"), active);
        };
    }
}
