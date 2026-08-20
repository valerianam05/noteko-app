package com.example.demo.mapper;

import com.example.demo.dto.response.CourseResponse;
import com.example.demo.entity.Course;

public class CourseMapper {

  private CourseMapper() {}

  public static CourseResponse toResponse(Course course) {
    return new CourseResponse(
        course.getId(),
        course.getCode(),
        course.getTitle(),
        course.getCredits(),
        course.getParcours(),
        course.getSemester().getId());
  }
}
