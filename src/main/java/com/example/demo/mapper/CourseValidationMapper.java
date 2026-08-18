package com.example.demo.mapper;

import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.enums.SessionType;

public class CourseValidationMapper {

  public static CourseValidationResponse toResponse(CourseValidation entity) {
    if (entity == null) {
      return null;
    }
    return CourseValidationResponse.builder()
        .id(entity.getId())
        .studentId(entity.getStudent() != null ? entity.getStudent().getId() : null)
        .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
        .academicYearId(entity.getAcademicYear() != null ? entity.getAcademicYear() : null)
        .finalAverage(entity.getFinalAverage())
        .validated(entity.getValidated())
        .creditsObtained(entity.getCreditsObtained())
        .session(
            entity.getSession() != null ? SessionType.valueOf(entity.getSession().name()) : null)
        .build();
  }
}
