package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.dto.response.GraduationStatusResponse;
import com.example.demo.dto.response.TranscriptResponse;
import com.example.demo.service.DocumentExportService;
import com.example.demo.service.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students/{std}")
@RequiredArgsConstructor
@Validated
public class TranscriptController {

  private final TranscriptService transcriptService;
  private final DocumentExportService documentExportService;

  @GetMapping("/transcript")
  public ResponseEntity<TranscriptResponse> getTranscript(
      @PathVariable String std, @RequestParam(required = false) String semesterCode) {
    return ResponseEntity.ok(transcriptService.getTranscript(std, semesterCode));
  }

  @GetMapping("/graduation-status")
  public ResponseEntity<GraduationStatusResponse> getGraduationStatus(@PathVariable String std) {
    return ResponseEntity.ok(transcriptService.getGraduationStatus(std));
  }

  @PostMapping("/transcript/send-email")
  public ResponseEntity<DocumentExportResponse> sendTranscriptEmail(
      @PathVariable String std, @RequestParam(required = false) String semesterCode) {
    DocumentExportResponse response = documentExportService.exportTranscriptPdf(std, semesterCode);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }
}
