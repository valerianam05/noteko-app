package com.example.demo.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeHistory {
  private UUID id;
  private UUID gradeId;
  private Double oldScore;
  private Double newScore;
  private String reason;
  private UUID modifiedBy;
  private OffsetDateTime modifiedAt;
}
