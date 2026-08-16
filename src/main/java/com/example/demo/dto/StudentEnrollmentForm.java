package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentEnrollmentForm {

    @NotNull(message = "L'étudiant est obligatoire")
    private UUID studentId;

    @NotNull(message = "Le groupe est obligatoire")
    private UUID groupId;

    @NotNull(message = "Le semestre est obligatoire")
    private UUID semesterId;

    @NotNull(message = "L'année universitaire est obligatoire")
    private UUID academicYearId;
}