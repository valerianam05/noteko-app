package com.example.demo.entity;

import com.example.demo.entity.enums.SessionType;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "course_validation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseValidation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id")
  private Student student;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "academic_year_id")
  private AcademicYear academicYear;

  @Column(name = "final_average", nullable = false)
  private Double finalAverage;

  @Column(name = "validated", nullable = false)
  private Boolean validated;

  @Column(name = "credits_obtained", nullable = false)
  private Integer creditsObtained;

  @Enumerated(EnumType.STRING)
  @Column(name = "session", nullable = false)
  private SessionType session;
}
