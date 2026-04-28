package br.com.backend.specification;

import br.com.backend.entity.Classroom;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClassroomSpecification {
    
    public static Specification<Classroom> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Classroom> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }

            return cb.equal(root.get("active"), active);
        };
    }
}
