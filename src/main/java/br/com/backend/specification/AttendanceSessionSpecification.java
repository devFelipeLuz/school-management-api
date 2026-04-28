package br.com.backend.specification;

import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Classroom;
import br.com.backend.entity.Professor;
import br.com.backend.entity.TeachingAssignment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AttendanceSessionSpecification {

    public static Specification<AttendanceSession> withProfessor(String name) {
        return (root, query, cb) -> {

            if (AttendanceSession.class.equals(query.getResultType())) {
                root.fetch("teachingAssignment")
                        .fetch("professor");
            }

            if (name == null || name.isBlank()) {
                return null;
            }

            Join<AttendanceSession, Professor> professorJoin = root
                    .join("teachingAssignment")
                    .join("professor");

            return buildLikePredicate(cb, professorJoin.get("name"), name);
        };
    }

    public static Specification<AttendanceSession> withSubject(String subject) {
        return (root, query, cb) -> {

            if (AttendanceSession.class.equals(query.getResultType())) {
                root.fetch("teachingAssignment")
                        .fetch("subject");
            }

            if (subject == null || subject.isBlank()) {
                return null;
            }

            Join<AttendanceSession, Professor> subjectJoin = root
                    .join("teachingAssignment")
                    .join("subject");

            return buildLikePredicate(cb, subjectJoin.get("name"), subject);
        };
    }

    public static Specification<AttendanceSession> withClassroom(String classroom) {
        return (root, query, cb) -> {

            if (AttendanceSession.class.equals(query.getResultType())) {
                root.fetch("teachingAssignment")
                        .fetch("classroom");
            }

            if (classroom == null || classroom.isBlank()) {
                return null;
            }

            Join<TeachingAssignment, Classroom> classroomJoin = root
                    .join("teachingAssignment")
                    .join("classroom");

            return buildLikePredicate(cb, classroomJoin.get("name"), classroom);
        };
    }

    public static Specification<AttendanceSession> withDate(String date) {
        return (root, query, cb) -> {

            if (date == null || date.isBlank()) {
                return null;
            }

            return cb.equal(root.get("date"), date);
        };
    }

    public static Predicate buildLikePredicate(CriteriaBuilder cb, Path<String> field, String value) {
        List<Predicate> predicates = new ArrayList<>();

        String[] terms = value.toLowerCase().split("\\s+");

        for (String term : terms) {
            predicates.add(
                    cb.like(
                            cb.lower(field),
                            "%" + term + "%"
                    )
            );
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
