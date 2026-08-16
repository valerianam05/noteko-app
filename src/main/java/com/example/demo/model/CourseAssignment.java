package com.example.demo.model;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseAssignment {
  private UUID id;
  private UUID courseId;
  private UUID teacherId;
  private UUID groupId;
  private UUID academicYearId;
}
