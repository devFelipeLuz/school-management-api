package br.com.backend.service;

import br.com.backend.builders.ClassroomBuilder;
import br.com.backend.builders.SchoolYearBuilder;
import br.com.backend.builders.StudentBuilder;
import br.com.backend.builders.UserBuilder;
import br.com.backend.dto.request.EnrollmentRequest;
import br.com.backend.entity.*;
import br.com.backend.entity.enums.Role;
import br.com.backend.exception.BusinessException;
import br.com.backend.exception.EntityNotFoundException;
import br.com.backend.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private SchoolYearService schoolYearService;

    @Mock
    private ClassroomService classroomService;

    @Mock
    private EnrollmentRepository repository;

    @InjectMocks
    private EnrollmentService service;

    private UUID studentId;
    private UUID schoolYearId;
    private UUID classroomId;

    private Student student;
    private SchoolYear schoolYear;
    private Classroom classroom;

    private EnrollmentRequest request;


    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        schoolYearId = UUID.randomUUID();
        classroomId = UUID.randomUUID();

        student = StudentBuilder.builder().build();
        schoolYear = SchoolYearBuilder.builder().build();
        classroom = ClassroomBuilder.builder().build();

        request = new EnrollmentRequest(studentId, schoolYearId, classroomId);
    }

    @Test
    void shouldEnrollStudentSuccessfully() {
        when(studentService.findActiveStudentById(studentId)).thenReturn(student);
        when(schoolYearService.findActiveSchoolYear(schoolYearId)).thenReturn(schoolYear);
        when(classroomService.findActiveClassroomById(classroomId)).thenReturn(classroom);
        when(repository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.enroll(request);

        verify(repository).save(any(Enrollment.class));
    }

    @Test
    void shouldThrowExceptionWhenClassroomIsFull() {
        when(studentService.findActiveStudentById(studentId)).thenReturn(student);
        when(schoolYearService.findActiveSchoolYear(schoolYearId)).thenReturn(schoolYear);
        when(classroomService.findActiveClassroomById(classroomId)).thenReturn(classroom);

        classroom.changeCapacity(1);
        classroom.increaseActiveEnrollmentsCount();

        assertThrows(BusinessException.class, () -> service.enroll(request));
    }

    @Test
    void shouldCancelEnrollment() {
        Enrollment enrollment = new Enrollment(student, schoolYear, classroom);

        when(repository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));

        service.cancel(enrollment.getId());

        assertTrue(enrollment.isCancelled());
    }

    @Test
    void shouldFinishEnrollment() {
        Enrollment enrollment = new Enrollment(student, schoolYear, classroom);

        when(repository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));

        service.finishEnrollment(enrollment.getId());

        assertTrue(enrollment.isFinished());
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentNotFound() {
        UUID enrollmentId = UUID.randomUUID();

        when(repository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, ()-> service.cancel(enrollmentId));
    }

    @Test
    void shouldThrowExceptionWhenStudentIsInactive() {
        when(studentService.findActiveStudentById(studentId))
                .thenThrow(new BusinessException("Student is inactive"));

        assertThrows(BusinessException.class, ()-> service.cancel(studentId));
    }

    @Test
    void shouldNotSaveEnrollmentWhenClassroomIsFull() {
        when(studentService.findActiveStudentById(studentId)).thenReturn(student);
        when(schoolYearService.findActiveSchoolYear(schoolYearId)).thenReturn(schoolYear);
        when(classroomService.findActiveClassroomById(classroomId)).thenReturn(classroom);

        classroom.changeCapacity(1);
        classroom.increaseActiveEnrollmentsCount();

        assertThrows(BusinessException.class, () -> service.enroll(request));

        verify(repository, never()).save(any());
    }
}
