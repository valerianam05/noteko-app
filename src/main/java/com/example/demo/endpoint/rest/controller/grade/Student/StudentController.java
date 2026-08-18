package com.example.demo.endpoint.rest.controller.grade.Student;

import com.example.demo.dto.request.StudentCreateRequest;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;
  private final StudentMapper studentMapper;

  @GetMapping
  public List<StudentResponse> list() {
    return studentService.findAll().stream().map(studentMapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  public StudentResponse detail(@PathVariable UUID id) {
    return studentMapper.toResponse(studentService.findById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StudentResponse create(@Valid @RequestBody StudentCreateRequest request) {
    Student student =
        studentService.create(
            request.email(),
            request.password(),
            request.firstName(),
            request.lastName(),
            request.stdNumber());
    return studentMapper.toResponse(student);
  }
}
