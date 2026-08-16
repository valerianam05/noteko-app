package com.example.demo.mapper;

import com.example.demo.dto.request.EvaluationRequest;
import com.example.demo.dto.response.EvaluationResponse;
import com.example.demo.model.Evaluation;

public final class EvaluationMapper {
  private EvaluationMapper() {}

  public static Evaluation toModel(EvaluationRequest request) {
    if (request == null) return null;
    return Evaluation.builder()
        .courseId(request.courseId())
        .title(request.title())
        .type(request.type())
        .weight(request.weight())
        .evaluationDate(request.evaluationDate())
        .build();
  }

  public static EvaluationResponse toResponse(Evaluation model) {
    if (model == null) return null;
    return new EvaluationResponse(
        model.getId(),
        model.getCourseId(),
        model.getTitle(),
        model.getType(),
        model.getWeight(),
        model.getEvaluationDate());
  }
}
