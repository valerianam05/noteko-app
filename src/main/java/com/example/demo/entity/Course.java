package com.example.demo.entity;

import com.example.demo.entity.enums.Parcours;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "code", nullable = false, unique = true)
  private String code;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "credits", nullable = false)
  private Integer credits;

  @Enumerated(EnumType.STRING)
  @Column(name = "parcours", nullable = false)
  private Parcours parcours;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "semester_id")
  private Semester semester;
}
