package com.example.demo.service;

import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.Student;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TranscriptPdfGenerator {

  public byte[] generateTranscriptPdf(
      String studentName,
      String stdNumber,
      String academicYear,
      double gpa,
      List<CourseValidation> validations)
      throws Exception {

    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    PdfWriter.getInstance(document, out);
    document.open();

    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
    Paragraph title = new Paragraph("RELEVÉ DE NOTES PROVISOIRE", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);
    document.add(new Paragraph(" "));

    Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
    document.add(new Paragraph("Étudiant : " + studentName + " (" + stdNumber + ")", infoFont));
    document.add(new Paragraph("Année Académique : " + academicYear, infoFont));
    document.add(
        new Paragraph("Moyenne Générale : " + String.format("%.2f", gpa) + " / 20", infoFont));
    document.add(new Paragraph(" "));

    PdfPTable table = new PdfPTable(4);
    table.setWidthPercentage(100);
    String[] headers = {"Cours", "Crédits", "Moyenne", "Validé"};
    for (String header : headers) {
      PdfPCell cell =
          new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
      cell.setBackgroundColor(Color.LIGHT_GRAY);
      table.addCell(cell);
    }

    if (validations != null) {
      for (CourseValidation validation : validations) {
        String courseTitle =
            (validation.getCourse() != null && validation.getCourse().getTitle() != null)
                ? validation.getCourse().getTitle()
                : "N/A";

        table.addCell(courseTitle);
        table.addCell(String.valueOf(validation.getCreditsObtained()));
        table.addCell(
            validation.getFinalAverage() != null
                ? String.format("%.2f", validation.getFinalAverage())
                : "-");
        table.addCell(Boolean.TRUE.equals(validation.getValidated()) ? "Oui" : "Non");
      }
    }

    document.add(table);
    document.close();

    return out.toByteArray();
  }

  public byte[] generateTranscriptPdf(
      Student student, String semesterCode, List<CourseValidation> validations) throws Exception {

    String studentName = student.getFirstName() + " " + student.getLastName();
    String stdNumber = student.getStdNumber();

    double gpa = 0.0;
    if (validations != null && !validations.isEmpty()) {
      double totalPoints = 0.0;
      int count = 0;
      for (CourseValidation v : validations) {
        if (v.getFinalAverage() != null) {
          totalPoints += v.getFinalAverage();
          count++;
        }
      }
      if (count > 0) {
        gpa = totalPoints / count;
      }
    }

    return generateTranscriptPdf(studentName, stdNumber, semesterCode, gpa, validations);
  }
}
