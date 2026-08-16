package com.example.demo.mapper;

import com.example.demo.dto.response.StudentEnrollmentResponse;
import com.example.demo.entity.StudentEnrollment;
import org.springframework.stereotype.Component;

@Component
public class StudentEnrollmentMapper {

  public StudentEnrollmentResponse toResponse(StudentEnrollment enrollment) {
    return new StudentEnrollmentResponse(
        enrollment.getId(),
        enrollment.getStudent().getAppUser().getFirstName()
            + " "
            + enrollment.getStudent().getAppUser().getLastName(),
        enrollment.getGroup().getName(),
        enrollment.getSemester().getCode().name(),
        enrollment.getAcademicYear().getLabel(),
        enrollment.getDateDebut(),
        enrollment.getDateFin());
  }
}
