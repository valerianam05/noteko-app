package com.example.demo.service;

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
import org.springframework.stereotype.Component;

@Component
public class TranscriptPdfGenerator {

  public byte[] generateTranscriptPdf(
      String studentName, String stdNumber, String academicYear, double gpa) throws Exception {
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

    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(100);

    String[] headers = {"Matière", "Crédits", "Note"};
    for (String header : headers) {
      PdfPCell cell =
          new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
      cell.setBackgroundColor(Color.LIGHT_GRAY);
      table.addCell(cell);
    }

    table.addCell("Architecture Logicielle & API");
    table.addCell("6");
    table.addCell("16.00");

    document.add(table);
    document.close();

    return out.toByteArray();
  }
}
