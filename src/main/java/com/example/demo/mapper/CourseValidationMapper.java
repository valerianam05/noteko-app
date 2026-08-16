package com.example.demo.mapper;

import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.model.CourseValidation;

public final class CourseValidationMapper {
  private CourseValidationMapper() {}

  public static CourseValidationResponse toResponse(CourseValidation model) {
    if (model == null) return null;
    return new CourseValidationResponse(
        model.getId(),
        model.getStudentId(),
        model.getCourseId(),
        model.getFinalAverage(),
        model.getValidated(),
        model.getCreditsObtained(),
        model.getSession(),
        model.getComputedAt());
  }
}
