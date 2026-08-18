package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.EvaluationRequest;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.CourseAssignment;
import com.example.demo.entity.Evaluation;
import com.example.demo.entity.Grade;
import com.example.demo.entity.enums.EvaluationType;
import com.example.demo.entity.enums.SessionType;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.EvaluationRepository;
import com.example.demo.repository.GradeRepository;
import com.example.demo.service.CourseAssignmentService;
import com.example.demo.service.EvaluationService;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

  @Mock private EvaluationRepository evaluationRepository;
  @Mock private CourseAssignmentService courseAssignmentService;
  @Mock private GradeRepository gradeRepository;

  @InjectMocks private EvaluationService evaluationService;

  private UUID evaluationId;
  private UUID courseAssignmentId;
  private CourseAssignment courseAssignment;
  private Evaluation evaluation;
  private AcademicYear academicYear;

  @BeforeEach
  void setUp() {
    evaluationId = UUID.randomUUID();
    courseAssignmentId = UUID.randomUUID();

    academicYear = AcademicYear.builder().id(UUID.randomUUID()).label("2025-2026").build();

    courseAssignment =
        CourseAssignment.builder().id(courseAssignmentId).academicYear(academicYear).build();

    evaluation =
        Evaluation.builder()
            .id(evaluationId)
            .courseAssignment(courseAssignment)
            .academicYear(academicYear)
            .title("Examen final")
            .type("EXAM")
            .coefficient(0.4)
            .dateEvaluation(OffsetDateTime.now())
            .build();
  }

  @Test
  @DisplayName("findById doit retourner l'évaluation si elle existe")
  void findById_Success() {
    when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));

    Evaluation result = evaluationService.findById(evaluationId);

    assertNotNull(result);
    assertEquals("Examen final", result.getTitle());
    assertEquals(0.4, result.getCoefficient());
    verify(evaluationRepository, times(1)).findById(evaluationId);
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si introuvable")
  void findById_NotFound() {
    when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> evaluationService.findById(evaluationId));
  }

  @Test
  @DisplayName("findEvaluations avec courseAssignmentId retourne les évaluations liées")
  void findEvaluations_WithCourseAssignmentId() {
    when(courseAssignmentService.findById(courseAssignmentId)).thenReturn(courseAssignment);
    when(evaluationRepository.findByCourseAssignment_Id(courseAssignmentId))
        .thenReturn(List.of(evaluation));

    List<Evaluation> results = evaluationService.findEvaluations(courseAssignmentId);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(courseAssignmentService, times(1)).findById(courseAssignmentId);
    verify(evaluationRepository, times(1)).findByCourseAssignment_Id(courseAssignmentId);
  }

  @Test
  @DisplayName("findEvaluations sans courseAssignmentId retourne toutes les évaluations")
  void findEvaluations_WithoutCourseAssignmentId() {
    when(evaluationRepository.findAll()).thenReturn(List.of(evaluation));

    List<Evaluation> results = evaluationService.findEvaluations(null);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(evaluationRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("create doit sauvegarder et retourner la nouvelle évaluation")
  void create_Success() {
    EvaluationRequest request =
        new EvaluationRequest(
            courseAssignmentId,
            "Projet Java",
            EvaluationType.EXAM,
            SessionType.NORMAL,
            0.3,
            OffsetDateTime.now());

    when(courseAssignmentService.findById(courseAssignmentId)).thenReturn(courseAssignment);
    when(evaluationRepository.save(any(Evaluation.class))).thenReturn(evaluation);

    Evaluation result = evaluationService.create(request);

    assertNotNull(result);
    verify(evaluationRepository, times(1)).save(any(Evaluation.class));
  }

  @Test
  @DisplayName(
      "create doit lever ValidationException si le weight est hors limites (< 0.01 ou > 1.00)")
  void create_InvalidWeight() {
    EvaluationRequest request =
        new EvaluationRequest(
            courseAssignmentId,
            "Devoir",
            EvaluationType.EXAM,
            SessionType.NORMAL,
            1.5,
            OffsetDateTime.now());

    assertThrows(ValidationException.class, () -> evaluationService.create(request));
  }

  @Test
  @DisplayName("update doit mettre à jour l'évaluation quand aucune note n'est publiée")
  void update_Success() {
    EvaluationRequest request =
        new EvaluationRequest(
            courseAssignmentId,
            "Rattrapage Java",
            EvaluationType.EXAM,
            SessionType.NORMAL,
            0.5,
            OffsetDateTime.now());

    when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));
    when(gradeRepository.findByEvaluationIdAndPublished(evaluationId, true))
        .thenReturn(Collections.emptyList());
    when(courseAssignmentService.findById(courseAssignmentId)).thenReturn(courseAssignment);
    when(evaluationRepository.save(any(Evaluation.class))).thenReturn(evaluation);

    Evaluation updated = evaluationService.update(evaluationId, request);

    assertNotNull(updated);
    verify(evaluationRepository, times(1)).save(evaluation);
  }

  @Test
  @DisplayName("update doit lever ConflictException si des notes sont déjà publiées")
  void update_Conflict_PublishedGradesExist() {
    EvaluationRequest request =
        new EvaluationRequest(
            courseAssignmentId,
            "Changement Titre",
            EvaluationType.EXAM,
            SessionType.NORMAL,
            0.4,
            OffsetDateTime.now());

    Grade publishedGrade = Grade.builder().published(true).build();

    when(evaluationRepository.findById(evaluationId)).thenReturn(Optional.of(evaluation));
    when(gradeRepository.findByEvaluationIdAndPublished(evaluationId, true))
        .thenReturn(List.of(publishedGrade));

    assertThrows(ConflictException.class, () -> evaluationService.update(evaluationId, request));
  }
}
