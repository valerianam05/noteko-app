package com.example.demo.service;

import com.example.demo.dto.request.EvaluationRequest;
import com.example.demo.entity.CourseAssignment;
import com.example.demo.entity.Evaluation;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.EvaluationRepository;
import com.example.demo.repository.GradeRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationService {

  private final EvaluationRepository evaluationRepository;
  private final CourseAssignmentService courseAssignmentService;
  private final GradeRepository gradeRepository;

  @Transactional(readOnly = true)
  public Evaluation findById(UUID id) {
    return evaluationRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Évaluation introuvable avec l'ID : " + id));
  }

  @Transactional(readOnly = true)
  public List<Evaluation> findEvaluations(UUID courseAssignmentId) {
    if (courseAssignmentId != null) {
      courseAssignmentService.findById(courseAssignmentId);
      return evaluationRepository.findByCourseAssignment_Id(courseAssignmentId);
    }
    return evaluationRepository.findAll();
  }

  public Evaluation create(EvaluationRequest request) {
    validerWeight(request.weight());

    CourseAssignment courseAssignment = courseAssignmentService.findById(request.courseId());

    Evaluation evaluation =
        Evaluation.builder()
            .courseAssignment(courseAssignment)
            .academicYear(courseAssignment.getAcademicYear())
            .title(request.title())
            .type(request.type() != null ? request.type().name() : null)
            .session(request.session())
            .coefficient(request.weight())
            .dateEvaluation(request.evaluationDate())
            .build();

    return evaluationRepository.save(evaluation);
  }

  public Evaluation update(UUID evaluationId, EvaluationRequest request) {
    validerWeight(request.weight());

    Evaluation evaluation = findById(evaluationId);

    boolean aDesNotesPubliees =
        !gradeRepository.findByEvaluationIdAndPublished(evaluationId, true).isEmpty();
    if (aDesNotesPubliees) {
      throw new ConflictException("Impossible de modifier une évaluation ayant des notes publiées");
    }

    CourseAssignment courseAssignment = courseAssignmentService.findById(request.courseId());

    evaluation.setCourseAssignment(courseAssignment);
    evaluation.setAcademicYear(courseAssignment.getAcademicYear());
    evaluation.setTitle(request.title());
    evaluation.setType(request.type() != null ? request.type().name() : null);
    evaluation.setSession(request.session());
    evaluation.setCoefficient(request.weight());
    evaluation.setDateEvaluation(request.evaluationDate());

    return evaluationRepository.save(evaluation);
  }

  private void validerWeight(Double weight) {
    if (weight == null || weight < 0.01 || weight > 1.00) {
      throw new ValidationException(
          "Pondération de l'évaluation doit être comprise entre 0.01 et 1.00");
    }
  }
}
