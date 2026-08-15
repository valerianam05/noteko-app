package com.example.demo.dto.request;

import com.example.demo.entity.enums.Parcours;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CourseRequest(
    @NotBlank String code,
    @NotBlank String name,
    @NotNull @Min(1) @Max(20) Integer credits,
    @NotNull UUID ueId,
    @NotNull Parcours parcours) {}
