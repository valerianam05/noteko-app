package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.EvaluationRequest;
import com.example.demo.dto.response.EvaluationResponse;
import com.example.demo.dto.response.GradeResponse;
import com.example.demo.entity.Evaluation;
import com.example.demo.mapper.EvaluationMapper;
import com.example.demo.mapper.GradeMapper;
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
  public ResponseEntity<List<EvaluationResponse>> getEvaluations(
      @RequestParam(required = false) UUID courseAssignmentId) {
    List<EvaluationResponse> responses =
        evaluationService.findEvaluations(courseAssignmentId).stream()
            .map(EvaluationMapper::toResponse)
            .toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EvaluationResponse> getEvaluationById(@PathVariable UUID id) {
    Evaluation evaluation = evaluationService.findById(id);
    return ResponseEntity.ok(EvaluationMapper.toResponse(evaluation));
  }

  @PostMapping
  public ResponseEntity<EvaluationResponse> createEvaluation(
      @Valid @RequestBody EvaluationRequest request) {
    Evaluation evaluation = evaluationService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(EvaluationMapper.toResponse(evaluation));
  }

  @PostMapping("/{id}/publish")
  public ResponseEntity<List<GradeResponse>> publishEvaluation(@PathVariable UUID id) {
    List<GradeResponse> responses =
        gradeService.publishGradesForEvaluation(id).stream()
            .map(GradeMapper::toModel)
            .map(GradeMapper::toResponse)
            .toList();
    return ResponseEntity.ok(responses);
  }
}
