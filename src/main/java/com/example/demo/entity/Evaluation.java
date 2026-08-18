package com.example.demo.entity;

import com.example.demo.entity.enums.SessionType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_assignment_id")
  private CourseAssignment courseAssignment;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "academic_year_id")
  private AcademicYear academicYear;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "type", nullable = false)
  private String type;

  @Enumerated(EnumType.STRING)
  @Column(name = "session", nullable = false)
  private SessionType session;

  @Column(name = "coefficient", nullable = false)
  private Double coefficient;

  @Column(name = "date_evaluation", nullable = false)
  private OffsetDateTime dateEvaluation;

  @Transient
  public UUID getCourseId() {
    if (courseAssignment != null && courseAssignment.getCourse() != null) {
      return courseAssignment.getCourse().getId();
    }
    return null;
  }
}
