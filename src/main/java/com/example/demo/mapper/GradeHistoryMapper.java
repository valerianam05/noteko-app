package com.example.demo.mapper;

import com.example.demo.dto.response.GradeHistoryResponse;
import com.example.demo.model.GradeHistory;

public final class GradeHistoryMapper {
  private GradeHistoryMapper() {}

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
