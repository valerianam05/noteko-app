package com.example.demo.dto.response;

import com.example.demo.entity.enums.AcademicLevel;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreditValidationResponse(
    UUID id,
    UUID studentId,
    AcademicLevel level,
    Integer totalCreditsObtained,
    Boolean levelValidated,
    OffsetDateTime validatedAt) {}
