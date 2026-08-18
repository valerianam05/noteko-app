package com.example.demo.dto.response;

import lombok.Builder;

@Builder
public record TranscriptResponse(
    String studentStd,
    String studentName,
    String semesterCode,
    String parcours,
    double generalAverage,
    int totalValidatedCredits,
    boolean isGraduated) {}
