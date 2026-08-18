package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.service.GraduatesExcelGenerator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraduatesDownloadController {

  private final GraduatesExcelGenerator excelGenerator;

  @GetMapping("/ui/promotions/{id}/graduates/excel")
  public ResponseEntity<byte[]> downloadGraduatesExcel(@PathVariable("id") UUID promotionId)
      throws Exception {
    byte[] excelBytes = excelGenerator.generate(promotionId);

    return ResponseEntity.ok()
        .contentType(
            MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=diplomes_promotion_" + promotionId + ".xlsx")
        .body(excelBytes);
  }
}
