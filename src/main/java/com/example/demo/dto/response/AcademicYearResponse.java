package com.example.demo.dto.response;

import java.util.UUID;

public record AcademicYearResponse(
        UUID id,
        String label,
        boolean isCurrent
) {}