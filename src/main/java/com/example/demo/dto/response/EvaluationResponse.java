package com.example.demo.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EvaluationResponse(
    UUID id,
    UUID courseId,
    String title,
    String type,
    Double coefficient,
    OffsetDateTime dateEvaluation) {}
