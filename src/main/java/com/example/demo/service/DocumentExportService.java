package com.example.demo.service;

import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.DocumentExport;
import com.example.demo.entity.Promotion;
import com.example.demo.entity.Student;
import com.example.demo.entity.enums.ExportFileType;
import com.example.demo.entity.enums.ExportStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.mapper.DocumentExportMapper;
import com.example.demo.repository.CourseValidationRepository;
import com.example.demo.repository.DocumentExportRepository;
import com.example.demo.service.event.S3Service;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
  private final CourseValidationRepository courseValidationRepository;
  private final PromotionService promotionService;
  private final StudentService studentService;
  private final AcademicYearService academicYearService;
  private final GraduatesExcelGenerator excelGenerator;
  private final TranscriptPdfGenerator transcriptPdfGenerator;
  private final S3Service s3Service;
  private final Mailer mailer;

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

      String presignedUrl =
          s3Service.uploadPdfAndGenerateUrl(excelBytes, fileName, promotionId.toString());

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

    Path temporaryDirectory = null;
    File pdfFile = null;

    try {
      Student student = studentService.findByStdNumber(std);

      List<CourseValidation> validations =
          courseValidationRepository.findByStudent_UserId(student.getUserId());
      byte[] pdfBytes =
          transcriptPdfGenerator.generateTranscriptPdf(student, semesterCode, validations);

      String fileName = "releve_notes_" + std + "_" + semesterCode + ".pdf";

      log.info("PDF du relevé généré : {}", fileName);

      temporaryDirectory = Files.createTempDirectory("transcript-export-");

      Path pdfPath = temporaryDirectory.resolve(fileName);

      Files.write(pdfPath, pdfBytes);

      pdfFile = pdfPath.toFile();

      log.info("Fichier PDF temporaire créé : {}", pdfFile.getAbsolutePath());

      String recipientEmail = student.getAppUser() != null ? student.getAppUser().getEmail() : null;

      if (recipientEmail == null || recipientEmail.isBlank()) {

        throw new IllegalStateException("L'étudiant " + std + " ne possède pas d'adresse email.");
      }

      String htmlBody =
          "<p>Bonjour "
              + student.getFirstName()
              + ",</p>"
              + "<p>Veuillez trouver ci-joint votre "
              + "relevé de notes pour le semestre "
              + semesterCode
              + ".</p>"
              + "<p>Votre relevé est disponible "
              + "au format PDF en pièce jointe.</p>"
              + "<p>Cordialement,<br/>"
              + "L'équipe pédagogique</p>";

      Email email =
          new Email(
              new InternetAddress(recipientEmail),
              List.of(),
              List.of(),
              "Votre relevé de notes - " + semesterCode,
              htmlBody,
              List.of(pdfFile));

      mailer.accept(email);

      log.info("Relevé de notes envoyé avec succès à {}", recipientEmail);
      export.setSentAt(OffsetDateTime.now().toLocalDateTime());

      export.setStatus(ExportStatus.GENERATED);

    } catch (Exception e) {

      log.error("Échec de la génération ou de l'envoi " + "du relevé PDF {}", exportId, e);
      export.setStatus(ExportStatus.FAILED);

    } finally {

      if (pdfFile != null) {
        try {
          Files.deleteIfExists(pdfFile.toPath());

          log.debug("Fichier PDF temporaire supprimé : {}", pdfFile.getAbsolutePath());

        } catch (IOException e) {

          log.warn(
              "Impossible de supprimer le fichier " + "PDF temporaire : {}",
              pdfFile.getAbsolutePath(),
              e);
        }
      }
      if (temporaryDirectory != null) {
        try {
          Files.deleteIfExists(temporaryDirectory);

        } catch (IOException e) {
          log.warn(
              "Impossible de supprimer le dossier " + "temporaire : {}", temporaryDirectory, e);
        }
      }

      documentExportRepository.save(export);
    }
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

    if (entity == null) {
      return null;
    }
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

  public List<CourseValidation> getValidations(String std) {

    Student student = studentService.findByStdNumber(std);

    return courseValidationRepository.findByStudent_UserId(student.getUserId());
  }
}
