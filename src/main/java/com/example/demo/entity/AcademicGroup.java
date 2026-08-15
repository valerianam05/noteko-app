package com.example.demo.entity;

import com.example.demo.entity.enums.Parcours;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "academic_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 50)
  private String name; // "K1", "K2", "K3"

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Parcours parcours;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "promotion_id")
  private Promotion promotion;
}
