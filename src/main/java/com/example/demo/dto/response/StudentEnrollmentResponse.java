package com.example.demo.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record StudentEnrollmentResponse(
        UUID id,
        String studentFullName,
        String groupName,
        String semesterCode,
        String academicYearLabel,
        LocalDate dateDebut,
        LocalDate dateFin
) {}