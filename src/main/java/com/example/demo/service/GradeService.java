package com.example.demo.service;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.request.GradeUpdateRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.GradeHistoryRepository;
import com.example.demo.repository.GradeRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

  private final GradeRepository gradeRepository;
  private final GradeHistoryRepository gradeHistoryRepository;
  private final EvaluationService evaluationService;
  private final StudentService studentService;
  private final AppUserRepository appUserRepository;

  @Transactional(readOnly = true)
  public Grade findById(UUID id) {
    return gradeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Note introuvable avec l'ID : " + id));
  }

  @Transactional(readOnly = true)
  public List<GradeHistory> findHistoryByGradeId(UUID gradeId) {
    findById(gradeId);
    return gradeHistoryRepository.findByGrade_IdOrderByModifiedAtDesc(gradeId);
  }

  public Grade createGrade(GradeRequest request) {
    validerScore(request.score());

    Evaluation evaluation = evaluationService.findById(request.evaluationId());
    Student student = studentService.findById(request.studentId());

    boolean noteExiste =
        gradeRepository.existsByStudentUserIdAndEvaluationId(
            request.studentId(), request.evaluationId());

    if (noteExiste) {
      throw new ConflictException("Une note existe déjà pour cet étudiant et cette évaluation");
    }

    Grade grade =
        Grade.builder()
            .student(student)
            .evaluation(evaluation)
            .score(request.score())
            .published(false)
            .build();

    return gradeRepository.save(grade);
  }

  public Grade updateGrade(UUID gradeId, GradeUpdateRequest request) {
    validerScore(request.score());

    Grade grade = findById(gradeId);

    UUID modifiedByUuid = UUID.fromString(request.modifiedByUserId());
    AppUser modifiedBy =
        appUserRepository
            .findById(modifiedByUuid)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Utilisateur modificateur introuvable avec l'ID : " + modifiedByUuid));

    GradeHistory history =
        GradeHistory.builder()
            .grade(grade)
            .oldValue(grade.getScore())
            .newValue(request.score())
            .modifiedBy(modifiedBy)
            .modifiedAt(OffsetDateTime.now())
            .reason(request.reason())
            .build();

    gradeHistoryRepository.save(history);

    grade.setScore(request.score());
    return gradeRepository.save(grade);
  }

  public void publishGradesForEvaluation(UUID evaluationId) {
    evaluationService.findById(evaluationId);

    List<Grade> notesBrouillon =
        gradeRepository.findAllByEvaluationIdAndPublishedFalse(evaluationId);

    notesBrouillon.forEach(
        grade -> {
          grade.setPublished(true);
        });

    gradeRepository.saveAll(notesBrouillon);
  }

  private void validerScore(Double score) {
    if (score == null || score < 0.0 || score > 20.0) {
      throw new ValidationException("La note doit être comprise entre 0.0 et 20.0");
    }
  }
}
