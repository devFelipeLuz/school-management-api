package br.com.backend.specification;

import br.com.backend.entity.SchoolYear;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SchoolYearSpecification {

    public static Specification<SchoolYear> yearContains(String year) {
        return (root, query, cb) -> {
            if (year == null || year.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.toString(root.get("year")),
                    "%" + year + "%"
            );
        };
    }

    public static Specification<SchoolYear> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }

            return cb.equal(root.get("active"), active);
        };
    }
}
