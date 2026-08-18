package com.example.demo.dto.response;

import java.util.Map;
import lombok.Builder;

@Builder
public record GraduationStatusResponse(
    String studentStd,
    String studentName,
    int totalRequiredCredits,
    int totalValidatedCredits,
    Map<String, Integer> creditsByLevel,
    boolean isGraduated) {}
