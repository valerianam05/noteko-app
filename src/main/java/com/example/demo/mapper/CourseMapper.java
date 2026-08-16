package com.example.demo.mapper;

import com.example.demo.dto.response.CourseResponse;
import com.example.demo.model.Course;

public final class CourseMapper {
  private CourseMapper() {}

  public static CourseResponse toResponse(Course model) {
    if (model == null) return null;

    return new CourseResponse(
        model.getId(),
        model.getCode(),
        model.getName(),
        model.getCredits(),
        model.getUeId(),
        model.getParcours());
  }
}
