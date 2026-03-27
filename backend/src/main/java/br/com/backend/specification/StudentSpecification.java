package br.com.backend.specification;

import br.com.backend.entity.Student;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class StudentSpecification {

    public static Specification<Student> withFilters(String name, String email, Boolean active) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                String[] names = name.toLowerCase().split("\\s+");

                for (String n: names) {
                    predicates.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("name")),
                                    "%" + n + "%"
                            )
                    );
                }
            }

            if (email != null && !email.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"
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
