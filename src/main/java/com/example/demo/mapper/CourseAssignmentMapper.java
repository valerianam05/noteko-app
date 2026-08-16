package com.example.demo.mapper;

import com.example.demo.dto.request.CourseAssignmentRequest;
import com.example.demo.dto.response.CourseAssignmentResponse;
import com.example.demo.model.CourseAssignment;
import java.util.UUID;

public final class CourseAssignmentMapper {
  private CourseAssignmentMapper() {}

  public static CourseAssignment toModel(CourseAssignmentRequest request) {
    if (request == null) return null;
    return CourseAssignment.builder()
        .id(UUID.randomUUID())
        .courseId(request.courseId())
        .teacherId(request.teacherId())
        .groupId(request.groupId())
        .academicYearId(request.academicYearId())
        .build();
  }

  public static CourseAssignmentResponse toResponse(CourseAssignment model) {
    if (model == null) return null;
    return new CourseAssignmentResponse(
        model.getId(),
        model.getCourseId(),
        model.getTeacherId(),
        model.getGroupId(),
        model.getAcademicYearId());
  }
}
