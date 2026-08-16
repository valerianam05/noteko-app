package com.example.demo.dto.request;

import com.example.demo.entity.enums.Parcours;
import java.util.UUID;
import lombok.Builder;

@Builder
public record AcademicGroupRequest(String name, Parcours parcours, UUID promotionId) {}
