package com.example.demo.mapper;

import com.example.demo.dto.response.EvaluationResponse;
import com.example.demo.entity.Evaluation; // Import de l'entité JPA

public final class EvaluationMapper {

  private EvaluationMapper() {}

  public static EvaluationResponse toResponse(Evaluation entity) {
    if (entity == null) return null;

    return new EvaluationResponse(
        entity.getId(),
        entity.getCourseAssignment() != null ? entity.getCourseAssignment().getId() : null,
        entity.getTitle(),
        entity.getType(),
        entity.getCoefficient(),
        entity.getDateEvaluation());
  }
}
