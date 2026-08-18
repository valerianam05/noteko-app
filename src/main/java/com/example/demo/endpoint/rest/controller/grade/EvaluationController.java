package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.EvaluationRequest;
import com.example.demo.entity.Evaluation;
import com.example.demo.service.EvaluationService;
import com.example.demo.service.GradeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

  private final EvaluationService evaluationService;
  private final GradeService gradeService;

  @GetMapping
  public ResponseEntity<List<Evaluation>> getEvaluations(
      @RequestParam(required = false) UUID courseAssignmentId) {
    return ResponseEntity.ok(evaluationService.findEvaluations(courseAssignmentId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Evaluation> getEvaluationById(@PathVariable UUID id) {
    return ResponseEntity.ok(evaluationService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Evaluation> createEvaluation(
      @Valid @RequestBody EvaluationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.create(request));
  }

  @PostMapping("/{id}/publish")
  public ResponseEntity<Void> publishEvaluation(@PathVariable UUID id) {
    gradeService.publishGradesForEvaluation(id);
    return ResponseEntity.noContent().build();
  }
}
