package br.com.backend.specification;

import br.com.backend.entity.SchoolYear;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SchoolYearSpecification {

    public static Specification<SchoolYear> withFilters(String year, Boolean active) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (year != null && !year.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.toString(root.get("year")),
                                "%" + year + "%"
                        )
                );
            }

            if (active != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("active"), active)
                );
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
