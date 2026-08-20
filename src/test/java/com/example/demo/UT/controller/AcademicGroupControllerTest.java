package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.AcademicGroupRequest;
import com.example.demo.dto.response.AcademicGroupResponse;
import com.example.demo.dto.response.StudentEnrollmentResponse;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.endpoint.rest.controller.grade.AcademicGroupController;
import com.example.demo.entity.AcademicGroup;
import com.example.demo.entity.Student;
import com.example.demo.entity.StudentEnrollment;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.mapper.AcademicGroupMapper;
import com.example.demo.mapper.StudentEnrollmentMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.AcademicGroupService;
import com.example.demo.service.StudentEnrollmentService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcademicGroupControllerTest {

  @Mock private AcademicGroupService academicGroupService;

  @Mock private AcademicGroupMapper academicGroupMapper;

  @Mock private StudentEnrollmentService studentEnrollmentService;

  @Mock private StudentMapper studentMapper;

  @Mock private StudentEnrollmentMapper studentEnrollmentMapper;

  @InjectMocks private AcademicGroupController academicGroupController;

  private UUID promotionId;
  private UUID groupId;
  private UUID studentId;
  private UUID enrollmentId;

  private AcademicGroup group;
  private Student student;
  private StudentEnrollment enrollment;

  private AcademicGroupResponse groupResponse;
  private StudentResponse studentResponse;
  private StudentEnrollmentResponse enrollmentResponse;

  @BeforeEach
  void setUp() {
    promotionId = UUID.randomUUID();
    groupId = UUID.randomUUID();
    studentId = UUID.randomUUID();
    enrollmentId = UUID.randomUUID();

    group = AcademicGroup.builder().id(groupId).name("L2-TN").build();

    student = Student.builder().userId(studentId).stdNumber("STD24020").build();

    enrollment = mock(StudentEnrollment.class);

    groupResponse = mock(AcademicGroupResponse.class);
    studentResponse = mock(StudentResponse.class);
    enrollmentResponse = mock(StudentEnrollmentResponse.class);
  }

  @Test
  @DisplayName("list doit retourner les groupes d'une promotion")
  void list_Success() {
    when(academicGroupService.findByPromotion(promotionId)).thenReturn(List.of(group));

    when(academicGroupMapper.toResponse(group)).thenReturn(groupResponse);

    List<AcademicGroupResponse> result = academicGroupController.list(promotionId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(groupResponse, result.get(0));

    verify(academicGroupService).findByPromotion(promotionId);

    verify(academicGroupMapper).toResponse(group);
  }

  @Test
  @DisplayName("studentsInGroup doit retourner les étudiants actifs du groupe")
  void studentsInGroup_Success() {

    when(enrollment.getStudent()).thenReturn(student);

    when(studentEnrollmentService.findActiveByGroup(groupId)).thenReturn(List.of(enrollment));

    when(studentMapper.toResponse(student)).thenReturn(studentResponse);

    List<StudentResponse> result = academicGroupController.studentsInGroup(groupId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(studentResponse, result.get(0));

    verify(studentEnrollmentService).findActiveByGroup(groupId);

    verify(enrollment).getStudent();

    verify(studentMapper).toResponse(student);
  }

  @Test
  @DisplayName("create doit créer un groupe et retourner sa réponse")
  void create_Success() {

    AcademicGroupRequest request = new AcademicGroupRequest("L2-TN", Parcours.TN, promotionId);

    when(academicGroupService.create(request.name(), request.parcours(), request.promotionId()))
        .thenReturn(group);

    when(academicGroupMapper.toResponse(group)).thenReturn(groupResponse);

    AcademicGroupResponse result = academicGroupController.create(request);

    assertNotNull(result);
    assertEquals(groupResponse, result);

    verify(academicGroupService).create(request.name(), request.parcours(), request.promotionId());

    verify(academicGroupMapper).toResponse(group);
  }

  @Test
  @DisplayName("close doit fermer une inscription et retourner la réponse")
  void close_Success() {

    LocalDate dateFin = LocalDate.of(2026, 8, 20);

    Map<String, LocalDate> body = Map.of("dateFin", dateFin);

    when(studentEnrollmentService.close(enrollmentId, dateFin)).thenReturn(enrollment);

    when(studentEnrollmentMapper.toResponse(enrollment)).thenReturn(enrollmentResponse);

    StudentEnrollmentResponse result = academicGroupController.close(enrollmentId, body);

    assertNotNull(result);
    assertEquals(enrollmentResponse, result);

    verify(studentEnrollmentService).close(enrollmentId, dateFin);

    verify(studentEnrollmentMapper).toResponse(enrollment);
  }
}
