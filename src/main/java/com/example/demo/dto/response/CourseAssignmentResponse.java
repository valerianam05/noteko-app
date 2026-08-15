package com.example.demo.dto.response;

import java.util.UUID;

public record CourseAssignmentResponse(
    UUID id, UUID courseId, UUID teacherId, UUID groupId, UUID academicYearId) {}
