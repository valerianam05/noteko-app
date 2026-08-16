package com.example.demo.model;

import com.example.demo.entity.enums.EvaluationType;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {
  private UUID id;
  private UUID courseId;
  private String title;
  private EvaluationType type;
  private Double weight;
  private OffsetDateTime evaluationDate;
}
