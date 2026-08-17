package com.example.demo.mapper;

import com.example.demo.dto.request.GradeRequest;
import com.example.demo.dto.response.GradeResponse;
import com.example.demo.model.Grade;

public final class GradeMapper {
  private GradeMapper() {}

  public static Grade toModel(com.example.demo.entity.Grade entity) {
    if (entity == null) return null;
    return Grade.builder()
        .id(entity.getId())
        .studentId(entity.getStudent().getUserId())
        .evaluationId(entity.getEvaluation().getId())
        .score(entity.getScore())
        .published(entity.getPublished())
        .publishedAt(entity.getPublishedAt())
        .build();
  }

  public static com.example.demo.entity.Grade toEntity(
      Grade model,
      com.example.demo.entity.Student student,
      com.example.demo.entity.Evaluation evaluation) {
    if (model == null) return null;
    return com.example.demo.entity.Grade.builder()
        .id(model.getId())
        .student(student)
        .evaluation(evaluation)
        .score(model.getScore())
        .published(Boolean.TRUE.equals(model.getPublished()))
        .publishedAt(model.getPublishedAt())
        .build();
  }

  public static Grade toModel(GradeRequest request) {
    if (request == null) return null;
    return Grade.builder()
        .studentId(request.studentId())
        .evaluationId(request.evaluationId())
        .score(request.score())
        .published(false)
        .publishedAt(null)
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
        model.getPublishedAt());
  }
}
