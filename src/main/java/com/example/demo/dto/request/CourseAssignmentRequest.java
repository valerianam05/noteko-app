package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CourseAssignmentRequest(
    @NotNull UUID courseId,
    @NotNull UUID teacherId,
    @NotNull UUID groupId,
    @NotNull UUID academicYearId) {}
