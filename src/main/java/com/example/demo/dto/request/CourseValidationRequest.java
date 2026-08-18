package com.example.demo.dto.request;

import java.util.UUID;

public record CourseValidationRequest(
    UUID studentId,
    UUID courseId,
    UUID academicYearId,
    Double finalAverage,
    Boolean validated,
    Integer creditsObtained,
    String session) {}
