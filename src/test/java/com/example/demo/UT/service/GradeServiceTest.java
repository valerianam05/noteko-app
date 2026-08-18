package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.request.GradeUpdateRequest;
import com.example.demo.entity.*;
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
    evaluation = Evaluation.builder().id(evaluationId).build();
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

  @Test
  @DisplayName("findById doit retourner la note si elle existe")
  void findById_Success() {
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));

    Grade result = gradeService.findById(gradeId);

    assertNotNull(result);
    assertEquals(14.0, result.getScore());
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si inexistante")
  void findById_NotFound() {
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> gradeService.findById(gradeId));
  }

  @Test
  @DisplayName("findHistoryByGradeId doit retourner l'historique si la note existe")
  void findHistoryByGradeId_Success() {
    GradeHistory history = GradeHistory.builder().id(UUID.randomUUID()).grade(grade).build();
    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
    when(gradeHistoryRepository.findByGrade_IdOrderByModifiedAtDesc(gradeId))
        .thenReturn(List.of(history));

    List<GradeHistory> result = gradeService.findHistoryByGradeId(gradeId);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("createGrade doit créer et sauvegarder la note")
  void createGrade_Success() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 15.0);

    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);
    when(studentService.findById(studentId)).thenReturn(student);
    when(gradeRepository.existsByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(false);
    when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

    Grade result = gradeService.createGrade(request);

    assertNotNull(result);
    verify(gradeRepository, times(1)).save(any(Grade.class));
  }

  @Test
  @DisplayName("createGrade doit lever ConflictException si la note existe déjà")
  void createGrade_Conflict() {
    GradeRequest request = new GradeRequest(studentId, evaluationId, 15.0);

    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);
    when(studentService.findById(studentId)).thenReturn(student);
    when(gradeRepository.existsByStudentUserIdAndEvaluationId(studentId, evaluationId))
        .thenReturn(true);

    assertThrows(ConflictException.class, () -> gradeService.createGrade(request));
  }

  @Test
  @DisplayName("createGrade doit lever ValidationException si score invalide (< 0 ou > 20)")
  void createGrade_InvalidScore() {
    GradeRequest requestInvalid = new GradeRequest(studentId, evaluationId, 25.0);

    assertThrows(ValidationException.class, () -> gradeService.createGrade(requestInvalid));
  }

  @Test
  @DisplayName("updateGrade doit créer un historique et mettre à jour le score")
  void updateGrade_Success() {
    UUID modifiedByUuid = UUID.randomUUID();

    GradeUpdateRequest request =
        new GradeUpdateRequest(16.0, "Erreur de correction", modifiedByUuid.toString());

    when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(grade));
    when(appUserRepository.findById(modifiedByUuid)).thenReturn(Optional.of(appUser));
    when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

    Grade updated = gradeService.updateGrade(gradeId, request);

    assertNotNull(updated);
    assertEquals(16.0, updated.getScore());
    verify(gradeHistoryRepository, times(1)).save(any(GradeHistory.class));
    verify(gradeRepository, times(1)).save(grade);
  }

  @Test
  @DisplayName("publishGradesForEvaluation passe les notes en publié")
  void publishGradesForEvaluation_Success() {
    when(evaluationService.findById(evaluationId)).thenReturn(evaluation);
    when(gradeRepository.findAllByEvaluationIdAndPublishedFalse(evaluationId))
        .thenReturn(List.of(grade));

    gradeService.publishGradesForEvaluation(evaluationId);

    assertTrue(grade.getPublished());
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
  }
}
