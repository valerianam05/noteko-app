package com.example.demo.model;

import com.example.demo.entity.enums.ExportFileType;
import com.example.demo.entity.enums.ExportStatus;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentExport {
  private UUID id;
  private UUID studentId;
  private UUID promotionId;
  private UUID academicYearId;
  private ExportFileType docType;
  private String s3Key;
  private ExportStatus status;
  private String errorMessage;
  private OffsetDateTime generatedAt;
  private OffsetDateTime sentAt;

  public boolean isValidTarget() {
    return (studentId != null) ^ (promotionId != null);
  }
}
