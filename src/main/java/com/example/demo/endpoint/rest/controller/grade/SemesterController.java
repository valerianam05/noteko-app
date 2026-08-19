package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.response.SemesterResponse;
import com.example.demo.mapper.SemesterMapper;
import com.example.demo.service.SemesterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;
  private final SemesterMapper semesterMapper;

  @GetMapping
  public List<SemesterResponse> list() {
    return semesterService.findAllOrdered().stream().map(semesterMapper::toResponse).toList();
  }
}
