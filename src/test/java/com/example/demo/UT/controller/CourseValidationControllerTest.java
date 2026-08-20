package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.CourseValidationRequest;
import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.endpoint.rest.controller.grade.CourseValidationController;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.enums.SessionType;
import com.example.demo.service.CourseValidationService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CourseValidationControllerTest {

  @Mock private CourseValidationService courseValidationService;

  @InjectMocks private CourseValidationController courseValidationController;

  private UUID studentId;
  private UUID courseId;
  private UUID academicYearId;
  private CourseValidationResponse response;

  @BeforeEach
  void setUp() {
    studentId = UUID.randomUUID();
    courseId = UUID.randomUUID();
    academicYearId = UUID.randomUUID();

    // Le record a @Builder, on l'utilise plutôt qu'un constructeur positionnel pour
    // éviter les erreurs d'ordre d'arguments rencontrées avec les autres records.
    response =
        CourseValidationResponse.builder()
            .id(UUID.randomUUID())
            .studentId(studentId)
            .courseId(courseId)
            .finalAverage(14.5)
            .validated(true)
            .creditsObtained(6)
            .session(SessionType.NORMAL)
            .academicYearId(new AcademicYear())
            .computedAt(OffsetDateTime.now())
            .build();
  }

  @Test
  @DisplayName("getCourseValidations doit retourner la liste filtrée")
  void getCourseValidations_Success() {
    when(courseValidationService.getCourseValidations(studentId, academicYearId))
        .thenReturn(List.of(response));

    ResponseEntity<List<CourseValidationResponse>> result =
        courseValidationController.getCourseValidations(studentId, academicYearId);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());
    assertEquals(1, result.getBody().size());

    verify(courseValidationService).getCourseValidations(studentId, academicYearId);
  }

  @Test
  @DisplayName("getCourseValidations doit retourner tout si aucun filtre")
  void getCourseValidations_NoFilter() {
    when(courseValidationService.getCourseValidations(null, null)).thenReturn(List.of(response));

    ResponseEntity<List<CourseValidationResponse>> result =
        courseValidationController.getCourseValidations(null, null);

    assertNotNull(result);
    assertEquals(1, result.getBody().size());
  }

  @Test
  @DisplayName("computeCourseValidation doit calculer et retourner 201")
  void computeCourseValidation_Success() {
    CourseValidationRequest request =
        new CourseValidationRequest(studentId, courseId, academicYearId, 14.5, true, 6, "NORMAL");

    when(courseValidationService.computeCourseValidation(any())).thenReturn(response);

    ResponseEntity<CourseValidationResponse> result =
        courseValidationController.computeCourseValidation(request);

    assertNotNull(result);
    assertEquals(201, result.getStatusCode().value());
    assertEquals(response, result.getBody());

    verify(courseValidationService).computeCourseValidation(request);
  }
}
