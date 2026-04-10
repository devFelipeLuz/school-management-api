package br.com.backend.builders.dto;

import br.com.backend.dto.request.EnrollmentRequest;

import java.util.UUID;

public class EnrollmentRequestBuilder {

    private UUID studentId = UUID.randomUUID();
    private UUID classroomId = UUID.randomUUID();

    public static EnrollmentRequestBuilder builder() {
        return new EnrollmentRequestBuilder();
    }

    public EnrollmentRequestBuilder withStudentId(UUID studentId) {
        this.studentId = studentId;
        return this;
    }

    public EnrollmentRequestBuilder withClassroomId(UUID classroomId) {
        this.classroomId = classroomId;
        return this;
    }

    public EnrollmentRequest build() {
        return new EnrollmentRequest(studentId, classroomId);
    }
}
