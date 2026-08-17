package com.example.demo.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {
  private UUID id;
  private UUID studentId;
  private UUID evaluationId;
  private Double score;
  private Boolean published;
  private OffsetDateTime createdAt;
  private OffsetDateTime publishedAt;

  public boolean isValidScore() {
    return score != null && score >= 0.0 && score <= 20.0;
  }
}
