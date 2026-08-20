package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.StudentEnrollmentRequest;
import com.example.demo.dto.response.StudentEnrollmentResponse;
import com.example.demo.endpoint.rest.controller.grade.StudentEnrollmentController;
import com.example.demo.entity.StudentEnrollment;
import com.example.demo.mapper.StudentEnrollmentMapper;
import com.example.demo.service.StudentEnrollmentService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentEnrollmentControllerTest {

  @Mock private StudentEnrollmentService studentEnrollmentService;

  @Mock private StudentEnrollmentMapper studentEnrollmentMapper;

  @InjectMocks private StudentEnrollmentController studentEnrollmentController;

  private UUID studentId;
  private UUID groupId;
  private UUID semesterId;
  private UUID academicYearId;

  private StudentEnrollment enrollment;
  private StudentEnrollmentResponse response;

  @BeforeEach
  void setUp() {
    studentId = UUID.randomUUID();
    groupId = UUID.randomUUID();
    semesterId = UUID.randomUUID();
    academicYearId = UUID.randomUUID();

    enrollment = mock(StudentEnrollment.class);
    response = mock(StudentEnrollmentResponse.class);
  }

  @Test
  @DisplayName("list doit retourner les inscriptions d'un étudiant")
  void list_Success() {
    when(studentEnrollmentService.findByStudent(studentId)).thenReturn(List.of(enrollment));

    when(studentEnrollmentMapper.toResponse(enrollment)).thenReturn(response);

    List<StudentEnrollmentResponse> result = studentEnrollmentController.list(studentId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(response, result.get(0));

    verify(studentEnrollmentService).findByStudent(studentId);

    verify(studentEnrollmentMapper).toResponse(enrollment);
  }

  @Test
  @DisplayName("create doit inscrire un étudiant et retourner la réponse")
  void create_Success() {
    StudentEnrollmentRequest request =
        new StudentEnrollmentRequest(studentId, groupId, semesterId, academicYearId);

    when(studentEnrollmentService.enroll(
            request.studentId(), request.groupId(), request.semesterId(), request.academicYearId()))
        .thenReturn(enrollment);

    when(studentEnrollmentMapper.toResponse(enrollment)).thenReturn(response);

    StudentEnrollmentResponse result = studentEnrollmentController.create(request);

    assertNotNull(result);
    assertEquals(response, result);

    verify(studentEnrollmentService)
        .enroll(
            request.studentId(), request.groupId(), request.semesterId(), request.academicYearId());

    verify(studentEnrollmentMapper).toResponse(enrollment);
  }
}
