package com.example.demo.dto.request;

import java.util.UUID;
import lombok.Builder;

@Builder
public record StudentEnrollmentRequest(
    UUID studentId, UUID groupId, UUID semesterId, UUID academicYearId) {}
