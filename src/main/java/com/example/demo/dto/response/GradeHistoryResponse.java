package com.example.demo.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record GradeHistoryResponse(
    UUID id,
    UUID gradeId,
    Double oldScore,
    Double newScore,
    String reason,
    UUID modifiedBy,
    OffsetDateTime modifiedAt) {}
