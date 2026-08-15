package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "academic_year")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 9)
  private String label; // généré automatiquement, ex: "2025-2026"

  @Column(name = "date_debut", nullable = false)
  private LocalDate dateDebut;

  @Column(name = "date_fin", nullable = false)
  private LocalDate dateFin;

  @Column(name = "is_current", nullable = false)
  private boolean isCurrent;

  @PrePersist
  @PreUpdate
  private void genererLabel() {
    if (dateDebut != null) {
      this.label = dateDebut.getYear() + "-" + (dateDebut.getYear() + 1);
    }
  }
}
