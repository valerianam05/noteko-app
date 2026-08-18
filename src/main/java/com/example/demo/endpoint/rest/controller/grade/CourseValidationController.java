package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.CourseValidationRequest;
import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.service.CourseValidationService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-validations")
@RequiredArgsConstructor
public class CourseValidationController {
  private final CourseValidationService courseValidationService;

  @GetMapping
  public ResponseEntity<List<CourseValidationResponse>> getCourseValidations(
      @RequestParam(required = false) UUID studentId,
      @RequestParam(required = false) UUID academicYearId) {
    return ResponseEntity.ok(
        courseValidationService.getCourseValidations(studentId, academicYearId));
  }

  @PostMapping
  public ResponseEntity<CourseValidationResponse> computeCourseValidation(
      @Valid @RequestBody CourseValidationRequest request) {
    CourseValidationResponse response = courseValidationService.computeCourseValidation(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
