package com.example.demo.mapper;

import com.example.demo.dto.response.CreditValidationResponse;
import com.example.demo.model.CreditValidation;

public final class CreditValidationMapper {
  private CreditValidationMapper() {}

  public static CreditValidationResponse toResponse(CreditValidation model) {
    if (model == null) return null;
    return new CreditValidationResponse(
        model.getId(),
        model.getStudentId(),
        model.getLevel(),
        model.getTotalCreditsObtained(),
        model.getLevelValidated(),
        model.getValidatedAt());
  }
}
