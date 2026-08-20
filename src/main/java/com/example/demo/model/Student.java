package com.example.demo.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
  private UUID id;
  private String stdNumber;
  private String firstName;
  private String lastName;
  private String email;
}
