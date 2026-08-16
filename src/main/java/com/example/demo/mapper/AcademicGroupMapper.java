package com.example.demo.mapper;

import com.example.demo.dto.response.AcademicGroupResponse;
import com.example.demo.entity.AcademicGroup;
import org.springframework.stereotype.Component;

@Component
public class AcademicGroupMapper {

  public AcademicGroupResponse toResponse(AcademicGroup group) {
    return new AcademicGroupResponse(
        group.getId(), group.getName(), group.getParcours(), group.getPromotion().getName());
  }
}
