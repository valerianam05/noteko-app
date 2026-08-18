package com.example.demo.service;

import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.entity.DocumentExport;
import com.example.demo.entity.Promotion;
import com.example.demo.entity.Student;
import com.example.demo.entity.enums.ExportFileType;
import com.example.demo.entity.enums.ExportStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.DocumentExportMapper;
import com.example.demo.repository.DocumentExportRepository;
import com.example.demo.service.event.S3Service;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
@Transactional
public class DocumentExportService {

  private static final Logger log = LoggerFactory.getLogger(DocumentExportService.class);

  private final DocumentExportRepository documentExportRepository;
  private final PromotionService promotionService;
  private final StudentService studentService;
  private final AcademicYearService academicYearService;
  private final GraduatesExcelGenerator excelGenerator;
  private final S3Service s3Service;

  @Transactional(readOnly = true)
  public List<DocumentExportResponse> findExports(
      UUID studentId, UUID promotionId, UUID academicYearId) {
    return findExportsByCriteria(studentId, promotionId, academicYearId).stream()
        .map(this::toModel)
        .map(DocumentExportMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public DocumentExportResponse findById(UUID id) {
    return DocumentExportMapper.toResponse(toModel(findEntityById(id)));
  }

  public DocumentExportResponse exportGraduates(UUID promotionId) {
    Promotion promotion = promotionService.findById(promotionId);

    DocumentExport export =
        DocumentExport.builder()
            .promotion(promotion)
            .academicYear(promotion.getAcademicYear())
            .docType(ExportFileType.GRADUATES_EXCEL)
            .status(ExportStatus.PENDING)
            .generatedAt(OffsetDateTime.now().toLocalDateTime())
            .build();

    DocumentExport saved = documentExportRepository.save(export);

    processGraduatesExportAsync(saved.getId(), promotionId);

    return DocumentExportMapper.toResponse(toModel(saved));
  }

  public DocumentExportResponse exportTranscriptPdf(String std, String semesterCode) {
    Student student = studentService.findByStdNumber(std);

    DocumentExport export =
        DocumentExport.builder()
            .student(student)
            .academicYear(academicYearService.getCurrent())
            .docType(ExportFileType.TRANSCRIPT_PDF)
            .status(ExportStatus.PENDING)
            .generatedAt(OffsetDateTime.now().toLocalDateTime())
            .build();

    DocumentExport saved = documentExportRepository.save(export);

    processTranscriptExportAsync(saved.getId(), std, semesterCode);

    return DocumentExportMapper.toResponse(toModel(saved));
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

  @Async
  public void processTranscriptExportAsync(UUID exportId, String std, String semesterCode) {
    DocumentExport export = documentExportRepository.findById(exportId).orElse(null);
    if (export == null) {
      log.error("Export introuvable pour ID : {}", exportId);
      return;
    }
    try {
      // TODO : génération PDF + upload S3 + envoi SES — reporté
      export.setStatus(ExportStatus.PENDING);
    } catch (Exception e) {
      log.error("Échec de la génération du relevé PDF {}", exportId, e);
      export.setStatus(ExportStatus.FAILED);
    }
    documentExportRepository.save(export);
  }

  private DocumentExport findEntityById(UUID id) {
    return documentExportRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Export introuvable avec l'ID : " + id));
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

  private com.example.demo.model.DocumentExport toModel(DocumentExport entity) {
    if (entity == null) return null;
    return com.example.demo.model.DocumentExport.builder()
        .id(entity.getId())
        .studentId(entity.getStudent() != null ? entity.getStudent().getUserId() : null)
        .promotionId(entity.getPromotion() != null ? entity.getPromotion().getId() : null)
        .academicYearId(entity.getAcademicYear() != null ? entity.getAcademicYear().getId() : null)
        .docType(entity.getDocType())
        .s3Key(entity.getS3Key())
        .status(entity.getStatus())
        .generatedAt(
            entity.getGeneratedAt() != null
                ? entity.getGeneratedAt().atOffset(ZoneOffset.UTC)
                : null)
        .sentAt(entity.getSentAt() != null ? entity.getSentAt().atOffset(ZoneOffset.UTC) : null)
        .build();
  }
}
