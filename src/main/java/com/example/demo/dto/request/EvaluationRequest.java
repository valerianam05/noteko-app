package com.example.demo.dto.request;

import com.example.demo.entity.enums.EvaluationType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EvaluationRequest(
    @NotNull UUID courseId,
    @NotBlank String title,
    @NotNull EvaluationType type,
    @NotNull @DecimalMin("0.01") @DecimalMax("1.00") Double weight,
    @NotNull OffsetDateTime evaluationDate) {}
