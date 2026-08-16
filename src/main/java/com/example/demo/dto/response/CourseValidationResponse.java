package com.example.demo.dto.response;

import com.example.demo.entity.enums.SessionType;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CourseValidationResponse(
    UUID id,
    UUID studentId,
    UUID courseId,
    Double finalAverage,
    Boolean validated,
    Integer creditsObtained,
    SessionType session,
    OffsetDateTime computedAt) {}
