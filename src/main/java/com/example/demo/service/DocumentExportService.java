package com.example.demo.service;

import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.entity.DocumentExport;
import com.example.demo.entity.Promotion;
import com.example.demo.entity.enums.ExportFileType;
import com.example.demo.entity.enums.ExportStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DocumentExportMapper;
import com.example.demo.repository.DocumentExportRepository;
import com.example.demo.service.event.S3Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentExportService {

  private static final Logger log = LoggerFactory.getLogger(DocumentExportService.class);

  private final DocumentExportRepository documentExportRepository;
  private final PromotionService promotionService;
  private final GraduatesExcelGenerator excelGenerator;
  private final S3Service s3Service;

  @Transactional(readOnly = true)
  public List<DocumentExportResponse> getDocumentExports(
      UUID studentId, UUID promotionId, UUID academicYearId) {
    List<DocumentExport> results = findExportsByCriteria(studentId, promotionId, academicYearId);
    return results.stream().map(DocumentExportMapper::toResponse).toList();
  }

  private List<DocumentExport> findExportsByCriteria(
      UUID studentId, UUID promotionId, UUID academicYearId) {
    if (studentId != null) {
      return documentExportRepository.findByStudentUserId(studentId);
    }
    if (promotionId != null) {
      return documentExportRepository.findByPromotion_Id(promotionId);
    }
    if (academicYearId != null) {
      return documentExportRepository.findByAcademicYear_Id(academicYearId);
    }
    return documentExportRepository.findAll();
  }

  @Transactional(readOnly = true)
  public DocumentExportResponse getDocumentExportById(UUID id) {
    DocumentExport export =
        documentExportRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Export introuvable avec l'ID : " + id));
    return DocumentExportMapper.toResponse(export);
  }

  @Transactional
  public DocumentExportResponse exportGraduates(UUID promotionId) {
    Promotion promotion = promotionService.findById(promotionId);

    DocumentExport export = new DocumentExport();
    export.setPromotion(promotion);
    export.setDocType(ExportFileType.GRADUATES_EXCEL);
    export.setStatus(ExportStatus.PENDING);
    export.setGeneratedAt(OffsetDateTime.now().toLocalDateTime());

    DocumentExport saved = documentExportRepository.save(export);

    processGraduatesExportAsync(saved.getId(), promotionId);

    return DocumentExportMapper.toResponse(saved);
  }

  @Async
  public void processGraduatesExportAsync(UUID exportId, UUID promotionId) {
    DocumentExport export = documentExportRepository.findById(exportId).orElse(null);
    if (export == null) {
      log.error("Export introuvable pour ID : {}", exportId);
      return;
    }

    try {
      byte[] excelBytes = excelGenerator.generate(promotionId);
      String fileName = "diplomes_promo_" + promotionId + ".xlsx";

      String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
      String presignedUrl =
          s3Service.uploadFileAndGenerateUrl(
              excelBytes, fileName, promotionId.toString(), contentType);

      export.setS3Key(presignedUrl);
      export.setStatus(ExportStatus.GENERATED);
    } catch (Exception e) {
      log.error("Échec de la génération de l'export diplômés {}", exportId, e);
      export.setStatus(ExportStatus.FAILED);
    }
    documentExportRepository.save(export);
  }
}
