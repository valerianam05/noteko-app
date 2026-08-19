package com.example.demo.IT;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.demo.conf.FacadeIT;
import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.DocumentExport;
import com.example.demo.entity.enums.ExportFileType;
import com.example.demo.entity.enums.ExportStatus;
import com.example.demo.mail.Mailer;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.repository.DocumentExportRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.service.AcademicYearService;
import com.example.demo.service.DocumentExportService;
import com.example.demo.service.GraduatesExcelGenerator;
import com.example.demo.service.PromotionService;
import com.example.demo.service.StudentService;
import com.example.demo.service.TranscriptPdfGenerator;
import com.example.demo.service.event.S3Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DocumentExportServiceIT extends FacadeIT {

  @Autowired private DocumentExportService documentExportService;

  @Autowired private DocumentExportRepository documentExportRepository;

  @Autowired private AcademicYearRepository academicYearRepository;

  @Autowired private PromotionRepository promotionRepository;

  @MockBean private PromotionService promotionService;

  @MockBean private StudentService studentService;

  @MockBean private AcademicYearService academicYearService;

  @MockBean private GraduatesExcelGenerator excelGenerator;

  @MockBean private TranscriptPdfGenerator transcriptPdfGenerator;

  @MockBean private S3Service s3Service;

  @MockBean private Mailer mailer;

  private AcademicYear persistedAcademicYear;

  @BeforeEach
  void cleanDatabase() {
    documentExportRepository.deleteAll();
    promotionRepository.deleteAll();
    academicYearRepository.deleteAll();

    persistedAcademicYear =
        academicYearRepository.save(
            AcademicYear.builder()
                .label("2025-2026")
                .dateDebut(LocalDate.of(2025, 9, 1))
                .dateFin(LocalDate.of(2026, 6, 30))
                .isCurrent(true)
                .build());
  }

  @Test
  void shouldFindExportById() {

    DocumentExport export =
        DocumentExport.builder()
            .academicYear(persistedAcademicYear)
            .docType(ExportFileType.GRADUATES_EXCEL)
            .status(ExportStatus.PENDING)
            .generatedAt(LocalDateTime.now())
            .build();

    DocumentExport saved = documentExportRepository.save(export);

    DocumentExportResponse response = documentExportService.findById(saved.getId());

    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(saved.getId());
    assertThat(response.docType()).isEqualTo(ExportFileType.GRADUATES_EXCEL);
    assertThat(response.status()).isEqualTo(ExportStatus.PENDING);
  }

  @Test
  void shouldFindAllExports() {

    DocumentExport export1 =
        DocumentExport.builder()
            .academicYear(persistedAcademicYear)
            .docType(ExportFileType.GRADUATES_EXCEL)
            .status(ExportStatus.PENDING)
            .generatedAt(LocalDateTime.now())
            .build();

    DocumentExport export2 =
        DocumentExport.builder()
            .academicYear(persistedAcademicYear)
            .docType(ExportFileType.TRANSCRIPT_PDF)
            .status(ExportStatus.GENERATED)
            .generatedAt(LocalDateTime.now())
            .build();

    documentExportRepository.save(export1);
    documentExportRepository.save(export2);

    var exports = documentExportService.findExports(null, null, null);

    assertThat(exports).hasSize(2);
    assertThat(exports)
        .extracting(DocumentExportResponse::status)
        .contains(ExportStatus.PENDING, ExportStatus.GENERATED);
  }

  @Test
  void shouldProcessGraduatesExport() throws Exception {

    UUID promotionId = UUID.randomUUID();

    DocumentExport export =
        DocumentExport.builder()
            .academicYear(persistedAcademicYear)
            .docType(ExportFileType.GRADUATES_EXCEL)
            .status(ExportStatus.PENDING)
            .generatedAt(LocalDateTime.now())
            .build();

    DocumentExport saved = documentExportRepository.save(export);

    byte[] excelBytes = "fake-excel-content".getBytes();

    when(excelGenerator.generate(promotionId)).thenReturn(excelBytes);

    when(s3Service.uploadPdfAndGenerateUrl(
            any(byte[].class), anyString(), eq(promotionId.toString())))
        .thenReturn("https://s3.test/export.xlsx");

    documentExportService.processGraduatesExportAsync(saved.getId(), promotionId);

    DocumentExport updated = documentExportRepository.findById(saved.getId()).orElseThrow();

    assertThat(updated.getStatus()).isEqualTo(ExportStatus.GENERATED);

    assertThat(updated.getS3Key()).isEqualTo("https://s3.test/export.xlsx");
  }
}
