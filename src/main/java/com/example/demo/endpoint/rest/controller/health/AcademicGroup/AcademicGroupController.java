package com.example.demo.endpoint.rest.controller.health.AcademicGroup;

import com.example.demo.dto.request.AcademicGroupRequest;
import com.example.demo.dto.response.AcademicGroupResponse;
import com.example.demo.entity.AcademicGroup;
import com.example.demo.mapper.AcademicGroupMapper;
import com.example.demo.service.AcademicGroupService;
import jakarta.validation.Valid;
import java.util.List;
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

  @GetMapping
  public List<AcademicGroupResponse> list(@RequestParam UUID promotionId) {
    return academicGroupService.findByPromotion(promotionId).stream()
        .map(academicGroupMapper::toResponse)
        .toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AcademicGroupResponse create(@Valid @RequestBody AcademicGroupRequest request) {
    AcademicGroup group =
        academicGroupService.create(request.name(), request.parcours(), request.promotionId());
    return academicGroupMapper.toResponse(group);
  }
}
