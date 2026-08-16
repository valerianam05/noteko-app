package com.example.demo.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GradeResponse(
    UUID id,
    UUID studentId,
    UUID evaluationId,
    Double score,
    Boolean published,
    OffsetDateTime createdAt) {}
