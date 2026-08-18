package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.GraduatesExportRequest;
import com.example.demo.dto.response.DocumentExportResponse;
import com.example.demo.service.DocumentExportService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/document-exports")
@RequiredArgsConstructor
public class DocumentExportController {
  private final DocumentExportService documentExportService;

  @GetMapping
  public ResponseEntity<List<DocumentExportResponse>> getDocumentExports(
      @RequestParam(required = false) UUID studentId,
      @RequestParam(required = false) UUID promotionId,
      @RequestParam(required = false) UUID academicYearId) {
    return ResponseEntity.ok(
        documentExportService.getDocumentExports(studentId, promotionId, academicYearId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<DocumentExportResponse> getDocumentExportById(@PathVariable UUID id) {
    return ResponseEntity.ok(documentExportService.getDocumentExportById(id));
  }

  @PostMapping("/graduates")
  public ResponseEntity<DocumentExportResponse> exportGraduates(
      @Valid @RequestBody GraduatesExportRequest request) {
    DocumentExportResponse response = documentExportService.exportGraduates(request.promotionId());
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }
}
