package com.example.demo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record GradeRequest(
    @NotNull UUID studentId, @NotNull UUID evaluationId, @NotNull @Min(0) @Max(20) Double score) {}
