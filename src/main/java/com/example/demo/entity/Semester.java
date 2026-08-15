package com.example.demo.entity;

import com.example.demo.entity.enums.AcademicLevel;
import com.example.demo.entity.enums.SemesterCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "semester")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true, length = 10)
  private SemesterCode code;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private AcademicLevel level;

  @Column(name = "order_num", nullable = false)
  private Integer orderNum;
}
