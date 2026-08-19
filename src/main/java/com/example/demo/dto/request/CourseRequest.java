package com.example.demo.dto.request;

import com.example.demo.entity.enums.Parcours;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record CourseRequest(
    @NotBlank String code,
    @NotBlank String title,
    @NotNull @Positive Integer credits,
    @NotNull Parcours parcours,
    @NotNull UUID semesterId) {}
