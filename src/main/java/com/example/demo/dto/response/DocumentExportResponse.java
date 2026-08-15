package com.example.demo.dto.response;

import com.example.demo.entity.enums.ExportFileType;
import com.example.demo.entity.enums.ExportStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentExportResponse(
    UUID id,
    UUID studentId,
    UUID promotionId,
    ExportFileType docType,
    String fileUrl,
    ExportStatus status,
    OffsetDateTime generatedAt) {}
