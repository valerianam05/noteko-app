package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

  @Id private UUID userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private AppUser appUser;

  @Column(name = "std_number", nullable = false, unique = true, length = 50)
  private String stdNumber;

  public UUID getId() {
    return this.userId;
  }

  public String getFirstName() {
    return (appUser != null) ? appUser.getFirstName() : "";
  }

  public String getLastName() {
    return (appUser != null) ? appUser.getLastName() : "";
  }

  @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
  private List<StudentEnrollment> enrollments;
}
