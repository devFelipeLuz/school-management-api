package br.com.backend.mapper;


import br.com.backend.DTO.response.EnrollmentResponseDTO;
import br.com.backend.entity.Enrollment;

public final class EnrollmentMapper {

    private EnrollmentMapper() {
        throw new UnsupportedOperationException("Mapper");
    }

    public static EnrollmentResponseDTO toDTO(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getStudent(),
                enrollment.getClassroom(),
                enrollment.getSchoolYear()
        );
    }
}
