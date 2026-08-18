package com.example.demo.mapper;

import com.example.demo.dto.request.DocumentExportRequest;
import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.model.DocumentExport;

public final class DocumentExportMapper {
  public static DocumentExport toModel(DocumentExportRequest request) {
    if (request == null) return null;

    return DocumentExport.builder()
        .studentId(request.studentId())
        .promotionId(request.promotionId())
        .academicYearId(request.academicYearId())
        .docType(request.docType())
        .build();
  }

  public static DocumentExportResponse toResponse(DocumentExport model) {
    if (model == null) return null;

    return new DocumentExportResponse(
        model.getId(),
        model.getStudentId(),
        model.getPromotionId(),
        model.getDocType(),
        model.getS3Key(),
        model.getStatus(),
        model.getGeneratedAt());
    //        model.getSentAt());
  }
}
