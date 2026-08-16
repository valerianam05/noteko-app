package com.example.demo.dto.request;

import com.example.demo.entity.enums.ExportFileType;
import java.util.UUID;

public record DocumentExportRequest(
    UUID studentId, UUID promotionId, UUID academicYearId, ExportFileType docType) {}
