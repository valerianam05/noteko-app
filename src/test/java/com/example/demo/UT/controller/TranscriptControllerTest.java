package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.response.GraduationStatusResponse;
import com.example.demo.dto.response.TranscriptResponse;
import com.example.demo.endpoint.rest.controller.grade.TranscriptController;
import com.example.demo.service.DocumentExportService;
import com.example.demo.service.TranscriptService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TranscriptControllerTest {

  @Mock private TranscriptService transcriptService;

  @Mock private DocumentExportService documentExportService;

  @InjectMocks private TranscriptController transcriptController;

  private String stdNumber;
  private TranscriptResponse transcriptResponse;
  private GraduationStatusResponse graduationStatusResponse;

  @BeforeEach
  void setUp() {
    stdNumber = "STD24020";

    // On utilise .builder() (comme dans le vrai service) plutôt qu'un constructeur
    // positionnel, pour éviter les erreurs d'ordre d'arguments qu'on a eues avec les
    // records (SemesterResponse, AcademicYearResponse...).
    transcriptResponse =
        TranscriptResponse.builder()
            .studentStd(stdNumber)
            .studentName("Henintsoa Maminiaina")
            .semesterCode("S1")
            .parcours("COMMON")
            .generalAverage(14.5)
            .totalValidatedCredits(30)
            .isGraduated(false)
            .build();

    graduationStatusResponse =
        GraduationStatusResponse.builder()
            .studentStd(stdNumber)
            .totalRequiredCredits(180)
            .totalValidatedCredits(60)
            .creditsByLevel(Map.of("L1", 60))
            .isGraduated(false)
            .build();
  }

  @Test
  @DisplayName("getTranscript doit retourner le relevé de notes de l'étudiant")
  void getTranscript_Success() {
    when(transcriptService.getTranscript(stdNumber, null)).thenReturn(transcriptResponse);

    ResponseEntity<TranscriptResponse> result = transcriptController.getTranscript(stdNumber, null);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());
    assertEquals(transcriptResponse, result.getBody());

    verify(transcriptService).getTranscript(stdNumber, null);
  }

  @Test
  @DisplayName("getTranscript doit filtrer par semestre si fourni")
  void getTranscript_WithSemesterFilter() {
    when(transcriptService.getTranscript(stdNumber, "S1")).thenReturn(transcriptResponse);

    ResponseEntity<TranscriptResponse> result = transcriptController.getTranscript(stdNumber, "S1");

    assertNotNull(result);
    assertEquals(transcriptResponse, result.getBody());

    verify(transcriptService).getTranscript(stdNumber, "S1");
  }

  @Test
  @DisplayName("getGraduationStatus doit retourner le statut de diplomation")
  void getGraduationStatus_Success() {
    when(transcriptService.getGraduationStatus(stdNumber)).thenReturn(graduationStatusResponse);

    ResponseEntity<GraduationStatusResponse> result =
        transcriptController.getGraduationStatus(stdNumber);

    assertNotNull(result);
    assertEquals(200, result.getStatusCode().value());
    assertEquals(graduationStatusResponse, result.getBody());

    verify(transcriptService).getGraduationStatus(stdNumber);
  }
}
