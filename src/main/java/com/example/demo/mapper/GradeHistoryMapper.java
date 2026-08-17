package com.example.demo.mapper;

import com.example.demo.dto.response.GradeHistoryResponse;
import com.example.demo.model.GradeHistory;

public final class GradeHistoryMapper {
  private GradeHistoryMapper() {}

  public static GradeHistory toModel(com.example.demo.entity.GradeHistory entity) {
    if (entity == null) return null;
    return GradeHistory.builder()
        .id(entity.getId())
        .gradeId(entity.getGrade().getId())
        .oldScore(entity.getOldValue())
        .newScore(entity.getNewValue())
        .modifiedBy(entity.getModifiedBy().getId())
        .modifiedAt(entity.getModifiedAt())
        .reason(entity.getReason())
        .build();
  }

  public static com.example.demo.entity.GradeHistory toEntity(
      GradeHistory model,
      com.example.demo.entity.Grade grade,
      com.example.demo.entity.AppUser modifiedBy) {
    if (model == null) return null;
    return com.example.demo.entity.GradeHistory.builder()
        .id(model.getId())
        .grade(grade)
        .oldValue(model.getOldScore())
        .newValue(model.getNewScore())
        .modifiedBy(modifiedBy)
        .modifiedAt(model.getModifiedAt())
        .reason(model.getReason())
        .build();
  }

  public static GradeHistoryResponse toResponse(GradeHistory model) {
    if (model == null) return null;
    return new GradeHistoryResponse(
        model.getId(),
        model.getGradeId(),
        model.getOldScore(),
        model.getNewScore(),
        model.getReason(),
        model.getModifiedBy(),
        model.getModifiedAt());
  }
}
