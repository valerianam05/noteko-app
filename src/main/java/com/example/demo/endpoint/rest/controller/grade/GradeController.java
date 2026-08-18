package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.request.GradeUpdateRequest;
import com.example.demo.dto.response.GradeHistoryResponse;
import com.example.demo.dto.response.GradeResponse;
import com.example.demo.entity.Grade;
import com.example.demo.mapper.GradeHistoryMapper;
import com.example.demo.mapper.GradeMapper;
import com.example.demo.service.GradeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

  private final GradeService gradeService;

  @GetMapping
  public ResponseEntity<List<GradeResponse>> getGrades(
      @RequestParam(required = false) UUID studentId,
      @RequestParam(required = false) UUID evaluationId,
      @RequestParam(required = false) Boolean published) {
    List<GradeResponse> responses =
        gradeService.findGrades(studentId, evaluationId, published).stream()
            .map(GradeMapper::toModel)
            .map(GradeMapper::toResponse)
            .toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{gradeId}")
  public ResponseEntity<GradeResponse> getGradeById(@PathVariable UUID gradeId) {
    Grade grade = gradeService.findById(gradeId);
    return ResponseEntity.ok(GradeMapper.toResponse(GradeMapper.toModel(grade)));
  }

  @PostMapping
  public ResponseEntity<GradeResponse> createGrade(@Valid @RequestBody GradeRequest request) {
    Grade grade = gradeService.createGrade(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(GradeMapper.toResponse(GradeMapper.toModel(grade)));
  }

  @PutMapping("/{gradeId}")
  public ResponseEntity<GradeResponse> updateGrade(
      @PathVariable UUID gradeId, @Valid @RequestBody GradeUpdateRequest request) {
    Grade grade = gradeService.updateGrade(gradeId, request);
    return ResponseEntity.ok(GradeMapper.toResponse(GradeMapper.toModel(grade)));
  }

  @GetMapping("/{gradeId}/history")
  public ResponseEntity<List<GradeHistoryResponse>> getGradeHistory(@PathVariable UUID gradeId) {
    List<GradeHistoryResponse> responses =
        gradeService.findHistoryByGradeId(gradeId).stream()
            .map(GradeHistoryMapper::toModel)
            .map(GradeHistoryMapper::toResponse)
            .toList();
    return ResponseEntity.ok(responses);
  }
}
