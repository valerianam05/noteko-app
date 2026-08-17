package com.example.demo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradeUpdateRequest(
    @NotNull @Min(0) @Max(20) Double score,
    @NotBlank String reason,
    @NotBlank String modifiedByUserId) {}
