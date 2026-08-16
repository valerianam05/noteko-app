package com.example.demo.endpoint.rest.controller.health.Teacher;

import com.example.demo.dto.request.TeacherCreateRequest;
import com.example.demo.dto.response.TeacherResponse;
import com.example.demo.entity.Teacher;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.service.TeacherService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

  private final TeacherService teacherService;
  private final TeacherMapper teacherMapper;

  @GetMapping
  public List<TeacherResponse> list() {
    return teacherService.findAll().stream().map(teacherMapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  public TeacherResponse detail(@PathVariable UUID id) {
    return teacherMapper.toResponse(teacherService.findById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TeacherResponse create(@Valid @RequestBody TeacherCreateRequest request) {
    Teacher teacher =
        teacherService.create(
            request.email(),
            request.password(),
            request.firstName(),
            request.lastName(),
            request.specialite());
    return teacherMapper.toResponse(teacher);
  }
}
