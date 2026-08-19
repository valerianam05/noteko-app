package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.StudentEnrollmentRequest;
import com.example.demo.dto.response.StudentEnrollmentResponse;
import com.example.demo.mapper.StudentEnrollmentMapper;
import com.example.demo.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

  private final StudentEnrollmentService studentEnrollmentService;
  private final StudentEnrollmentMapper studentEnrollmentMapper;

  @GetMapping
  public List<StudentEnrollmentResponse> list(@RequestParam UUID studentId) {
    return studentEnrollmentService.findByStudent(studentId).stream()
        .map(studentEnrollmentMapper::toResponse)
        .toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StudentEnrollmentResponse create(@Valid @RequestBody StudentEnrollmentRequest request) {
    var enrollment =
        studentEnrollmentService.enroll(
            request.studentId(), request.groupId(),
            request.semesterId(), request.academicYearId());
    return studentEnrollmentMapper.toResponse(enrollment);
  }
}
