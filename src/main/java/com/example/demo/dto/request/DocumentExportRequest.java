package com.example.demo.dto.request;

import com.example.demo.entity.enums.ExportFileType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DocumentExportRequest(
    UUID studentId,
    UUID promotionId,
    @NotNull UUID academicYearId,
    @NotNull ExportFileType docType) {}
