package com.example.demo.UT.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.endpoint.rest.controller.grade.GradeController;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Evaluation;
import com.example.demo.entity.Grade;
import com.example.demo.entity.GradeHistory;
import com.example.demo.entity.Student;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.security.filter.BearerAuthFilter;
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
    controllers = GradeController.class,
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BearerAuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class GradeControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private GradeService gradeService;

  private UUID gradeId;
  private UUID studentId;
  private UUID evaluationId;
  private Grade grade;
  private Student student;
  private Evaluation evaluation;

  @BeforeEach
  void setUp() {
    gradeId = UUID.randomUUID();
    studentId = UUID.randomUUID();
    evaluationId = UUID.randomUUID();

    student = Student.builder().userId(studentId).build();
    evaluation = Evaluation.builder().id(evaluationId).build();

    grade =
        Grade.builder()
            .id(gradeId)
            .student(student)
            .evaluation(evaluation)
            .score(15.5)
            .published(false)
            .build();
  }

  @Test
  @DisplayName("GET /api/grades doit retourner la liste des notes")
  void getGrades_Success() throws Exception {
    when(gradeService.findGrades(null, null, null)).thenReturn(List.of(grade));

    mockMvc
        .perform(get("/api/grades"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  @DisplayName("GET /api/grades/{id} doit retourner la note demandée")
  void getGradeById_Success() throws Exception {
    when(gradeService.findById(gradeId)).thenReturn(grade);

    mockMvc
        .perform(get("/api/grades/{gradeId}", gradeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(gradeId.toString()));
  }

  @Test
  @DisplayName("GET /api/grades/{id} doit retourner 404 si la note n'existe pas")
  void getGradeById_NotFound() throws Exception {
    when(gradeService.findById(gradeId))
        .thenThrow(new ResourceNotFoundException("Note introuvable avec l'ID : " + gradeId));

    mockMvc.perform(get("/api/grades/{gradeId}", gradeId)).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("POST /api/grades doit créer une note et retourner 201")
  void createGrade_Success() throws Exception {
    when(gradeService.createGrade(any())).thenReturn(grade);

    String requestBody =
        """
        {
          "studentId": "%s",
          "evaluationId": "%s",
          "score": 15.5
        }
        """
            .formatted(studentId, evaluationId);

    mockMvc
        .perform(post("/api/grades").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.score").value(15.5));
  }

  @Test
  @DisplayName("POST /api/grades doit retourner 409 si la note existe déjà")
  void createGrade_Conflict() throws Exception {
    when(gradeService.createGrade(any()))
        .thenThrow(
            new ConflictException("Une note existe déjà pour cet étudiant et cette évaluation"));

    String requestBody =
        """
        {
          "studentId": "%s",
          "evaluationId": "%s",
          "score": 15.5
        }
        """
            .formatted(studentId, evaluationId);

    mockMvc
        .perform(post("/api/grades").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isConflict());
  }

  @Test
  @DisplayName("POST /api/grades doit retourner 400 si le score est manquant")
  void createGrade_ValidationError() throws Exception {
    String requestBody =
        """
        {
          "studentId": "%s",
          "evaluationId": "%s"
        }
        """
            .formatted(studentId, evaluationId);

    mockMvc
        .perform(post("/api/grades").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("PUT /api/grades/{id} doit mettre à jour la note")
  void updateGrade_Success() throws Exception {
    when(gradeService.updateGrade(any(), any())).thenReturn(grade);

    String requestBody =
        """
        {
          "score": 17.0,
          "reason": "Erreur de correction",
          "modifiedByUserId": "%s"
        }
        """
            .formatted(UUID.randomUUID());

    mockMvc
        .perform(
            put("/api/grades/{gradeId}", gradeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/grades/{id}/history doit retourner l'historique")
  void getGradeHistory_Success() throws Exception {
    AppUser modifiedBy = AppUser.builder().id(UUID.randomUUID()).build();

    GradeHistory history =
        GradeHistory.builder()
            .id(UUID.randomUUID())
            .grade(grade)
            .oldValue(14.0)
            .newValue(15.5)
            .modifiedBy(modifiedBy)
            .modifiedAt(OffsetDateTime.now())
            .reason("Erreur de correction")
            .build();

    when(gradeService.findHistoryByGradeId(gradeId)).thenReturn(List.of(history));

    mockMvc
        .perform(get("/api/grades/{gradeId}/history", gradeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}
