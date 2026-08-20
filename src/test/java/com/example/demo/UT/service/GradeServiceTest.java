package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.request.GradeUpdateRequest;
<<<<<<< HEAD
import com.example.demo.entity.*;
=======
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Evaluation;
import com.example.demo.entity.Grade;
import com.example.demo.entity.GradeHistory;
import com.example.demo.entity.Student;
>>>>>>> origin/preprod
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.GradeHistoryRepository;
import com.example.demo.repository.GradeRepository;
import com.example.demo.service.EvaluationService;
import com.example.demo.service.GradeService;
import com.example.demo.service.StudentService;
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
class GradeServiceTest {

  @Mock private GradeRepository gradeRepository;
  @Mock private GradeHistoryRepository gradeHistoryRepository;
  @Mock private EvaluationService evaluationService;
  @Mock private StudentService studentService;
  @Mock private AppUserRepository appUserRepository;

  @InjectMocks private GradeService gradeService;

  private UUID gradeId;
  private UUID studentId;
  private UUID evaluationId;
  private UUID userId;
<<<<<<< HEAD
=======

>>>>>>> origin/preprod
  private Grade grade;
  private Student student;
  private Evaluation evaluation;
  private AppUser appUser;

  @BeforeEach
  void setUp() {
    gradeId = UUID.randomUUID();
    studentId = UUID.randomUUID();
    evaluationId = UUID.randomUUID();
    userId = UUID.randomUUID();

    student = Student.builder().userId(studentId).build();
<<<<<<< HEAD
    evaluation = Evaluation.builder().id(evaluationId).build();
=======

    evaluation = Evaluation.builder().id(evaluationId).coefficient(1.0).build();

>>>>>>> origin/preprod
    appUser = AppUser.builder().id(userId).build();

    grade =
        Grade.builder()
            .id(gradeId)
            .student(student)
            .evaluation(evaluation)
            .score(14.0)
            .published(false)
            .build();
  }

<<<<<<< HEAD
=======
  // =========================================================
  // findById
  // =========================================================

>>>>>>> origin/preprod
  @Test
  @DisplayName("findById doit retourner la note si elle existe")
  void findById_Success() {
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));

    Grade result = gradeService.findById(gradeId);

    assertNotNull(result);
<<<<<<< HEAD
=======
    assertEquals(gradeId, result.getId());
>>>>>>> origin/preprod
    assertEquals(14.0, result.getScore());
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si inexistante")
  void findById_NotFound() {
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> gradeService.findById(gradeId));
  }

<<<<<<< HEAD
=======
  // =========================================================
  // findHistoryByGradeId
  // =========================================================

>>>>>>> origin/preprod
  @Test
  @DisplayName("findHistoryByGradeId doit retourner l'historique si la note existe")
  void findHistoryByGradeId_Success() {
    GradeHistory history = GradeHistory.builder().id(UUID.randomUUID()).grade(grade).build();
<<<<<<< HEAD
=======

>>>>>>> origin/preprod
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
    when(gradeHistoryRepository.findByGrade_IdOrderByModifiedAtDesc(gradeId))
        .thenReturn(List.of(history));

    List<GradeHistory> result = gradeService.findHistoryByGradeId(gradeId);

    assertNotNull(result);
    assertEquals(1, result.size());
<<<<<<< HEAD
  }

  @Test
=======
    assertEquals(history, result.get(0));
  }

  @Test
  @DisplayName(
      "findHistoryByGradeId doit lever ResourceNotFoundException si la note est inexistante")
  void findHistoryByGradeId_NotFound() {
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> gradeService.findHistoryByGradeId(gradeId));

    verifyNoInteractions(gradeHistoryRepository);
  }

  // =========================================================
  // createGrade
  // =========================================================

  @Test
>>>>>>> origin/preprod
  @DisplayName("createGrade doit créer et sauvegarder la note")
  void createGrade_Success() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 15.0);

    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);
<<<<<<< HEAD
    when(studentService.findById(studentId)).thenReturn(student);
    when(gradeRepository.existsByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(false);
=======

    when(studentService.findById(studentId)).thenReturn(student);

    when(gradeRepository.existsByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(false);

>>>>>>> origin/preprod
    when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

    Grade result = gradeService.createGrade(request);

    assertNotNull(result);
<<<<<<< HEAD
    verify(gradeRepository, times(1)).save(any(Grade.class));
=======
    assertEquals(grade, result);

    verify(evaluationService).findById(evaluationId);
    verify(studentService).findById(studentId);

    verify(gradeRepository).existsByStudentUserIdAndEvaluationId(studentId, evaluationId);

    verify(gradeRepository).save(any(Grade.class));
>>>>>>> origin/preprod
  }

  @Test
  @DisplayName("createGrade doit lever ConflictException si la note existe déjà")
  void createGrade_Conflict() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 15.0);

    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);
<<<<<<< HEAD
    when(studentService.findById(studentId)).thenReturn(student);
=======

    when(studentService.findById(studentId)).thenReturn(student);

>>>>>>> origin/preprod
    when(gradeRepository.existsByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(true);

    assertThrows(ConflictException.class, () -> gradeService.createGrade(request));
<<<<<<< HEAD
  }

  @Test
  @DisplayName("createGrade doit lever ValidationException si score invalide (< 0 ou > 20)")
  void createGrade_InvalidScore() {
    GradeRequest requestInvalid = new GradeRequest(studentId, evaluationId, 25.0);

    assertThrows(ValidationException.class, () -> gradeService.createGrade(requestInvalid));
  }

  @Test
=======

    verify(gradeRepository, never()).save(any(Grade.class));
  }

  @Test
  @DisplayName("createGrade doit lever ValidationException si score supérieur à 20")
  void createGrade_ScoreTooHigh() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 25.0);

    assertThrows(ValidationException.class, () -> gradeService.createGrade(request));

    verifyNoInteractions(evaluationService, studentService, gradeRepository);
  }

  @Test
  @DisplayName("createGrade doit lever ValidationException si score négatif")
  void createGrade_NegativeScore() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, -1.0);

    assertThrows(ValidationException.class, () -> gradeService.createGrade(request));

    verifyNoInteractions(evaluationService, studentService, gradeRepository);
  }

  @Test
  @DisplayName("createGrade doit lever ValidationException si score null")
  void createGrade_NullScore() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, null);

    assertThrows(ValidationException.class, () -> gradeService.createGrade(request));

    verifyNoInteractions(evaluationService, studentService, gradeRepository);
  }

  @Test
  @DisplayName("createGrade doit propager l'erreur si l'évaluation est introuvable")
  void createGrade_EvaluationNotFound() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 15.0);

    when(evaluationService.findById(evaluationId))
        .thenThrow(new ResourceNotFoundException("Évaluation introuvable"));

    assertThrows(ResourceNotFoundException.class, () -> gradeService.createGrade(request));

    verify(studentService, never()).findById(any());
    verify(gradeRepository, never()).save(any());
  }

  @Test
  @DisplayName("createGrade doit propager l'erreur si le student est introuvable")
  void createGrade_StudentNotFound() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 15.0);

    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);

    when(studentService.findById(studentId))
        .thenThrow(new ResourceNotFoundException("Student introuvable"));

    assertThrows(ResourceNotFoundException.class, () -> gradeService.createGrade(request));

    verify(gradeRepository, never()).existsByStudentUserIdAndEvaluationId(any(), any());

    verify(gradeRepository, never()).save(any());
  }

  // =========================================================
  // updateGrade
  // =========================================================

  @Test
>>>>>>> origin/preprod
  @DisplayName("updateGrade doit créer un historique et mettre à jour le score")
  void updateGrade_Success() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request =
        new GradeUpdateRequest(16.0, "Erreur de correction", modifiedByUuid.toString());

    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
<<<<<<< HEAD
    when(appUserRepository.findById(modifiedByUuid)).thenReturn(Optional.of(appUser));
=======

    when(appUserRepository.findById(modifiedByUuid)).thenReturn(Optional.of(appUser));

>>>>>>> origin/preprod
    when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

    Grade updated = gradeService.updateGrade(gradeId, request);

    assertNotNull(updated);
    assertEquals(16.0, updated.getScore());
<<<<<<< HEAD
    verify(gradeHistoryRepository, times(1)).save(any(GradeHistory.class));
    verify(gradeRepository, times(1)).save(grade);
  }

  @Test
  @DisplayName("publishGradesForEvaluation passe les notes en publié")
  void publishGradesForEvaluation_Success() {
    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);
=======

    verify(gradeHistoryRepository).save(any(GradeHistory.class));

    verify(gradeRepository).save(grade);
  }

  @Test
  @DisplayName("updateGrade doit lever ResourceNotFoundException si la note est inexistante")
  void updateGrade_GradeNotFound() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request =
        new GradeUpdateRequest(16.0, "Erreur de correction", modifiedByUuid.toString());

    when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> gradeService.updateGrade(gradeId, request));

    verifyNoInteractions(appUserRepository, gradeHistoryRepository);
  }

  @Test
  @DisplayName(
      "updateGrade doit lever ResourceNotFoundException si le modificateur est introuvable")
  void updateGrade_ModifierNotFound() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request =
        new GradeUpdateRequest(16.0, "Erreur de correction", modifiedByUuid.toString());

    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));

    when(appUserRepository.findById(modifiedByUuid)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> gradeService.updateGrade(gradeId, request));

    verify(gradeHistoryRepository, never()).save(any(GradeHistory.class));

    verify(gradeRepository, never()).save(any(Grade.class));
  }

  @Test
  @DisplayName("updateGrade doit rejeter un score supérieur à 20")
  void updateGrade_ScoreTooHigh() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request = new GradeUpdateRequest(21.0, "Erreur", modifiedByUuid.toString());

    assertThrows(ValidationException.class, () -> gradeService.updateGrade(gradeId, request));

    verifyNoInteractions(gradeRepository, appUserRepository, gradeHistoryRepository);
  }

  @Test
  @DisplayName("updateGrade doit rejeter un score négatif")
  void updateGrade_NegativeScore() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request = new GradeUpdateRequest(-1.0, "Erreur", modifiedByUuid.toString());

    assertThrows(ValidationException.class, () -> gradeService.updateGrade(gradeId, request));

    verifyNoInteractions(gradeRepository, appUserRepository, gradeHistoryRepository);
  }

  @Test
  @DisplayName("updateGrade doit rejeter un score null")
  void updateGrade_NullScore() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request = new GradeUpdateRequest(null, "Erreur", modifiedByUuid.toString());

    assertThrows(ValidationException.class, () -> gradeService.updateGrade(gradeId, request));

    verifyNoInteractions(gradeRepository, appUserRepository, gradeHistoryRepository);
  }

  // =========================================================
  // publishGradesForEvaluation
  // =========================================================

  @Test
  @DisplayName("publishGradesForEvaluation passe les notes en publié")
  void publishGradesForEvaluation_Success() {
    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);

>>>>>>> origin/preprod
    when(gradeRepository.findAllByEvaluationIdAndPublishedFalse(evaluationId))
        .thenReturn(List.of(grade));

    gradeService.publishGradesForEvaluation(evaluationId);

    assertTrue(grade.getPublished());
<<<<<<< HEAD
    verify(gradeRepository, times(1)).saveAll(anyList());
  }

  @Test
  @DisplayName("findGrades teste les différentes combinaisons de filtres")
  void findGrades_Combinations() {
    when(gradeRepository.findByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(List.of(grade));
    List<Grade> res1 = gradeService.findGrades(studentId, evaluationId, null);
    assertEquals(1, res1.size());

    when(gradeRepository.findByStudentUserId(studentId)).thenReturn(List.of(grade));
    List<Grade> res2 = gradeService.findGrades(studentId, null, null);
    assertEquals(1, res2.size());

    when(gradeRepository.findAll()).thenReturn(List.of(grade));
    List<Grade> res3 = gradeService.findGrades(null, null, null);
    assertEquals(1, res3.size());
=======

    verify(gradeRepository).saveAll(anyList());
  }

  @Test
  @DisplayName("publishGradesForEvaluation doit gérer une liste vide")
  void publishGradesForEvaluation_Empty() {
    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);

    when(gradeRepository.findAllByEvaluationIdAndPublishedFalse(evaluationId))
        .thenReturn(List.of());

    gradeService.publishGradesForEvaluation(evaluationId);

    verify(gradeRepository).saveAll(List.of());
  }

  @Test
  @DisplayName("publishGradesForEvaluation doit propager l'erreur si l'évaluation est introuvable")
  void publishGradesForEvaluation_EvaluationNotFound() {
    when(evaluationService.findById(evaluationId))
        .thenThrow(new ResourceNotFoundException("Évaluation introuvable"));

    assertThrows(
        ResourceNotFoundException.class,
        () -> gradeService.publishGradesForEvaluation(evaluationId));

    verify(gradeRepository, never()).findAllByEvaluationIdAndPublishedFalse(any());
  }

  // =========================================================
  // findGrades
  // =========================================================

  @Test
  @DisplayName("findGrades avec studentId et evaluationId sans filtre publié")
  void findGrades_StudentAndEvaluation() {
    when(gradeRepository.findByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(List.of(grade));

    List<Grade> result = gradeService.findGrades(studentId, evaluationId, null);

    assertEquals(1, result.size());

    verify(gradeRepository).findByStudentUserIdAndEvaluationId(studentId, evaluationId);
  }

  @Test
  @DisplayName("findGrades avec studentId et evaluationId et published=true")
  void findGrades_StudentAndEvaluation_PublishedTrue() {
    Grade publishedGrade =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation)
            .score(15.0)
            .published(true)
            .build();

    Grade unpublishedGrade =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation)
            .score(10.0)
            .published(false)
            .build();

    when(gradeRepository.findByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(List.of(publishedGrade, unpublishedGrade));

    List<Grade> result = gradeService.findGrades(studentId, evaluationId, true);

    assertEquals(1, result.size());
    assertTrue(result.get(0).getPublished());
  }

  @Test
  @DisplayName("findGrades avec studentId et evaluationId et published=false")
  void findGrades_StudentAndEvaluation_PublishedFalse() {
    Grade publishedGrade =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation)
            .score(15.0)
            .published(true)
            .build();

    Grade unpublishedGrade =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation)
            .score(10.0)
            .published(false)
            .build();

    when(gradeRepository.findByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(List.of(publishedGrade, unpublishedGrade));

    List<Grade> result = gradeService.findGrades(studentId, evaluationId, false);

    assertEquals(1, result.size());
    assertFalse(result.get(0).getPublished());
  }

  @Test
  @DisplayName("findGrades avec studentId seulement")
  void findGrades_StudentOnly() {
    when(gradeRepository.findByStudentUserId(studentId)).thenReturn(List.of(grade));

    List<Grade> result = gradeService.findGrades(studentId, null, null);

    assertEquals(1, result.size());

    verify(gradeRepository).findByStudentUserId(studentId);
  }

  @Test
  @DisplayName("findGrades avec evaluationId et published=true")
  void findGrades_EvaluationAndPublished() {
    when(gradeRepository.findByEvaluationIdAndPublished(evaluationId, true))
        .thenReturn(List.of(grade));

    List<Grade> result = gradeService.findGrades(null, evaluationId, true);

    assertEquals(1, result.size());

    verify(gradeRepository).findByEvaluationIdAndPublished(evaluationId, true);
  }

  @Test
  @DisplayName("findGrades avec evaluationId sans filtre published")
  void findGrades_EvaluationOnly() {
    when(gradeRepository.findByEvaluationId(evaluationId)).thenReturn(List.of(grade));

    List<Grade> result = gradeService.findGrades(null, evaluationId, null);

    assertEquals(1, result.size());

    verify(gradeRepository).findByEvaluationId(evaluationId);
  }

  @Test
  @DisplayName("findGrades avec published seulement")
  void findGrades_PublishedOnly() {
    when(gradeRepository.findByPublished(true)).thenReturn(List.of(grade));

    List<Grade> result = gradeService.findGrades(null, null, true);

    assertEquals(1, result.size());

    verify(gradeRepository).findByPublished(true);
  }

  @Test
  @DisplayName("findGrades sans aucun filtre")
  void findGrades_NoFilter() {
    when(gradeRepository.findAll()).thenReturn(List.of(grade));

    List<Grade> result = gradeService.findGrades(null, null, null);

    assertEquals(1, result.size());

    verify(gradeRepository).findAll();
  }

  // =========================================================
  // calculateStudentAverage
  // =========================================================

  @Test
  @DisplayName("calculateStudentAverage doit retourner 0 si aucune note publiée")
  void calculateStudentAverage_NoPublishedGrades() {
    Grade unpublishedGrade =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation)
            .score(15.0)
            .published(false)
            .build();

    when(gradeRepository.findByStudentUserId(studentId)).thenReturn(List.of(unpublishedGrade));

    Double result = gradeService.calculateStudentAverage(studentId);

    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("calculateStudentAverage doit retourner 0 si aucune note")
  void calculateStudentAverage_NoGrades() {
    when(gradeRepository.findByStudentUserId(studentId)).thenReturn(List.of());

    Double result = gradeService.calculateStudentAverage(studentId);

    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("calculateStudentAverage doit calculer la moyenne pondérée")
  void calculateStudentAverage_Success() {
    Evaluation evaluation1 = Evaluation.builder().id(UUID.randomUUID()).coefficient(1.0).build();

    Evaluation evaluation2 = Evaluation.builder().id(UUID.randomUUID()).coefficient(2.0).build();

    Grade grade1 =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation1)
            .score(10.0)
            .published(true)
            .build();

    Grade grade2 =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(evaluation2)
            .score(20.0)
            .published(true)
            .build();

    when(gradeRepository.findByStudentUserId(studentId)).thenReturn(List.of(grade1, grade2));

    Double result = gradeService.calculateStudentAverage(studentId);

    assertEquals(16.6666666667, result, 0.000001);
  }

  @Test
  @DisplayName("calculateStudentAverage doit retourner 0 si la somme des coefficients vaut 0")
  void calculateStudentAverage_ZeroCoefficient() {
    Evaluation zeroCoefficientEvaluation =
        Evaluation.builder().id(UUID.randomUUID()).coefficient(0.0).build();

    Grade zeroCoefficientGrade =
        Grade.builder()
            .id(UUID.randomUUID())
            .student(student)
            .evaluation(zeroCoefficientEvaluation)
            .score(15.0)
            .published(true)
            .build();

    when(gradeRepository.findByStudentUserId(studentId)).thenReturn(List.of(zeroCoefficientGrade));

    Double result = gradeService.calculateStudentAverage(studentId);

    assertEquals(0.0, result);
>>>>>>> origin/preprod
  }
}
