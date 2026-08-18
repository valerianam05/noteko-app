package com.example.demo.service;

import com.example.demo.dto.response.GraduationStatusResponse;
import com.example.demo.entity.Student;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GraduatesExcelGenerator {

  private final StudentService studentService;
  private final TranscriptService transcriptService;

  public byte[] generate(UUID promotionId) throws Exception {
    List<Student> students = studentService.findByPromotionId(promotionId);

    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      Sheet sheet = workbook.createSheet("Diplômés");
      Row header = sheet.createRow(0);
      header.createCell(0).setCellValue("STD");
      header.createCell(1).setCellValue("Nom et Prénoms");
      header.createCell(2).setCellValue("Statut Diplomation");

      int rowIndex = 1;
      for (Student student : students) {
        GraduationStatusResponse status =
            transcriptService.getGraduationStatus(student.getStdNumber());

        if (!status.isGraduated()) {
          continue;
        }

        Row row = sheet.createRow(rowIndex++);
        row.createCell(0)
            .setCellValue(student.getStdNumber() != null ? student.getStdNumber() : "");

        String fullName =
            (student.getLastName() != null ? student.getLastName() : "")
                + " "
                + (student.getFirstName() != null ? student.getFirstName() : "");
        row.createCell(1).setCellValue(fullName.trim());
        row.createCell(2).setCellValue("Diplômé (180 Crédits)");
      }

      for (int i = 0; i < 3; i++) {
        sheet.autoSizeColumn(i);
      }

      workbook.write(out);
      return out.toByteArray();
    }
  }
}
