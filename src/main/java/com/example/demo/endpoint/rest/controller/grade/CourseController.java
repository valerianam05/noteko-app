package com.example.demo.endpoint.rest.controller.grade;

import com.example.demo.dto.request.CourseRequest;
import com.example.demo.dto.response.CourseResponse;
import com.example.demo.entity.Course;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.service.CourseService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @GetMapping
  public ResponseEntity<List<CourseResponse>> getCourses() {
    List<CourseResponse> responses =
        courseService.findAll().stream().map(CourseMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{courseId}")
  public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID courseId) {
    Course course = courseService.findById(courseId);
    return ResponseEntity.ok(CourseMapper.toResponse(course));
  }

  @PostMapping
  public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
    Course course = courseService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(CourseMapper.toResponse(course));
  }
}
