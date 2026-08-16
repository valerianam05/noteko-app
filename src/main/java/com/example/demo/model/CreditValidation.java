package com.example.demo.model;

import com.example.demo.entity.enums.AcademicLevel;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditValidation {
  private UUID id;
  private UUID studentId;
  private AcademicLevel level;
  private Integer totalCreditsObtained;
  private Boolean levelValidated;
  private OffsetDateTime validatedAt;
}
