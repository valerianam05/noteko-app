package com.example.demo.mapper;

import com.example.demo.dto.response.SemesterResponse;
import com.example.demo.entity.Semester;
import org.springframework.stereotype.Component;

@Component
public class SemesterMapper {
  public SemesterResponse toResponse(Semester semester) {
    return new SemesterResponse(
        semester.getId(), semester.getCode(), semester.getLevel(), semester.getOrderNum());
  }
}
