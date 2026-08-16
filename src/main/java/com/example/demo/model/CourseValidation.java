package com.example.demo.model;

import com.example.demo.entity.enums.SessionType;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseValidation {
  private UUID id;
  private UUID studentId;
  private UUID courseId;
  private UUID academicYearId;
  private Double finalAverage;
  private Boolean validated;
  private Integer creditsObtained;
  private SessionType session;
  private OffsetDateTime computedAt;

  public static boolean isPassing(double average) {
    return average >= 10.0;
  }
}
