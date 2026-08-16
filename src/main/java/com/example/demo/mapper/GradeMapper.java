package com.example.demo.mapper;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.response.GradeResponse;
import com.example.demo.model.Grade;

public final class GradeMapper {
  private GradeMapper() {}

  public static Grade toModel(GradeRequest request) {
    if (request == null) return null;
    return Grade.builder()
        .studentId(request.studentId())
        .evaluationId(request.evaluationId())
        .score(request.score())
        .build();
  }

  public static GradeResponse toResponse(Grade model) {
    if (model == null) return null;
    return new GradeResponse(
        model.getId(),
        model.getStudentId(),
        model.getEvaluationId(),
        model.getScore(),
        model.getPublished(),
        model.getCreatedAt());
  }
}
