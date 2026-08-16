package com.example.demo.mapper;

import com.example.demo.dto.response.AcademicYearResponse;
import com.example.demo.entity.AcademicYear;
import org.springframework.stereotype.Component;

@Component
public class AcademicYearMapper {

  public AcademicYearResponse toResponse(AcademicYear academicYear) {
    return new AcademicYearResponse(
        academicYear.getId(), academicYear.getLabel(), academicYear.isCurrent());
  }
}
