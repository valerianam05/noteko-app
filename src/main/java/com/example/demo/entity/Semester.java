package com.example.demo.entity;

import com.example.demo.entity.enums.AcademicLevel;
import com.example.demo.entity.enums.SemesterCode;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "semester")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 10)
    private SemesterCode code; // S1..S6

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AcademicLevel level; // L1/L2/L3

    @Column(name = "order_num", nullable = false)
    private Integer orderNum;
}