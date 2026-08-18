package com.example.demo.endpoint.rest.controller.grade.AcademicGroup;

import com.example.demo.dto.request.AcademicGroupRequest;
import com.example.demo.dto.response.AcademicGroupResponse;
import com.example.demo.dto.response.StudentEnrollmentResponse;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.AcademicGroup;
import com.example.demo.mapper.AcademicGroupMapper;
import com.example.demo.mapper.StudentEnrollmentMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.AcademicGroupService;
import com.example.demo.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class AcademicGroupController {

  private final AcademicGroupService academicGroupService;
  private final AcademicGroupMapper academicGroupMapper;

  private final StudentEnrollmentService studentEnrollmentService;
  private final StudentMapper studentMapper;

  private final StudentEnrollmentMapper studentEnrollmentMapper;

  @GetMapping
  public List<AcademicGroupResponse> list(@RequestParam UUID promotionId) {
    return academicGroupService.findByPromotion(promotionId).stream()
        .map(academicGroupMapper::toResponse)
        .toList();
  }

  @GetMapping("/{id}/students")
  public List<StudentResponse> studentsInGroup(@PathVariable UUID id) {
    return studentEnrollmentService.findActiveByGroup(id).stream()
        .map(enrollment -> studentMapper.toResponse(enrollment.getStudent()))
        .toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AcademicGroupResponse create(@Valid @RequestBody AcademicGroupRequest request) {
    AcademicGroup group =
        academicGroupService.create(request.name(), request.parcours(), request.promotionId());
    return academicGroupMapper.toResponse(group);
  }

  // Dans StudentEnrollmentController, ajouter :
  @PatchMapping("/{id}")
  public StudentEnrollmentResponse close(
      @PathVariable UUID id, @RequestBody Map<String, LocalDate> body) {
    return studentEnrollmentMapper.toResponse(
        studentEnrollmentService.close(id, body.get("dateFin")));
  }
}
