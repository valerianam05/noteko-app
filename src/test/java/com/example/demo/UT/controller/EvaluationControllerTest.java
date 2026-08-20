package com.example.demo.UT.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.endpoint.rest.controller.grade.EvaluationController;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseAssignment;
import com.example.demo.entity.Evaluation;
import com.example.demo.entity.enums.SessionType;
import com.example.demo.security.filter.BearerAuthFilter;
import com.example.demo.service.EvaluationService;
import com.example.demo.service.GradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = EvaluationController.class,
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BearerAuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class EvaluationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private EvaluationService evaluationService;
  @MockBean private GradeService gradeService;

  private UUID evaluationId;
  private UUID courseAssignmentId;
  private Evaluation evaluation;

  @BeforeEach
  void setUp() {
    evaluationId = UUID.randomUUID();
    courseAssignmentId = UUID.randomUUID();

    CourseAssignment courseAssignment = new CourseAssignment();
    courseAssignment.setId(courseAssignmentId);
    courseAssignment.setCourse(new Course());
    courseAssignment.setAcademicYear(new AcademicYear());

    evaluation =
        Evaluation.builder()
            .id(evaluationId)
            .courseAssignment(courseAssignment)
            .academicYear(new AcademicYear())
            .title("Examen final")
            .type("EXAM")
            .session(SessionType.NORMAL)
            .coefficient(0.5)
            .dateEvaluation(OffsetDateTime.now())
            .build();
  }

  @Test
  @DisplayName("GET /api/evaluations doit retourner la liste des évaluations")
  void getEvaluations_Success() throws Exception {
    when(evaluationService.findEvaluations(null)).thenReturn(List.of(evaluation));

    mockMvc.perform(get("/api/evaluations")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/evaluations/{id} doit retourner l'évaluation demandée")
  void getEvaluationById_Success() throws Exception {
    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);

    mockMvc.perform(get("/api/evaluations/{id}", evaluationId)).andExpect(status().isOk());
  }

  @Test
  @DisplayName("POST /api/evaluations doit créer une évaluation et retourner 201")
  void createEvaluation_Success() throws Exception {
    when(evaluationService.create(any())).thenReturn(evaluation);

    String requestBody =
        """
        {
          "courseId": "%s",
          "title": "Examen final",
          "type": "EXAM",
          "session": "NORMAL",
          "weight": 0.5,
          "evaluationDate": "2026-08-25T09:00:00+03:00"
        }
        """
            .formatted(courseAssignmentId);

    mockMvc
        .perform(
            post("/api/evaluations").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("POST /api/evaluations doit retourner 400 si le poids est manquant")
  void createEvaluation_ValidationError() throws Exception {
    String requestBody =
        """
        {
          "courseId": "%s",
          "title": "Examen final",
          "type": "EXAM",
          "session": "NORMAL",
          "evaluationDate": "2026-08-25T09:00:00+03:00"
        }
        """
            .formatted(courseAssignmentId);

    mockMvc
        .perform(
            post("/api/evaluations").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("POST /api/evaluations/{id}/publish doit publier les notes et retourner 204")
  void publishEvaluation_Success() throws Exception {
    mockMvc
        .perform(post("/api/evaluations/{id}/publish", evaluationId))
        .andExpect(status().isNoContent());
  }
}
