package com.example.demo.dto.response;

import com.example.demo.entity.enums.EvaluationType;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EvaluationResponse(
    UUID id,
    UUID courseId,
    String title,
    EvaluationType type,
    Double weight,
    OffsetDateTime evaluationDate) {}
