package com.example.demo.model;

import com.example.demo.entity.enums.Parcours;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
  private UUID id;
  private String code;
  private String name;
  private Integer credits;
  private UUID ueId;
  private Parcours parcours;

  public boolean isValidCredits() {
    return credits != null && credits > 0 && credits <= 20;
  }
}
