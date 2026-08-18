package com.example.demo.dto.response;

import com.example.demo.entity.enums.Parcours;
import java.util.UUID;

public record AcademicGroupResponse(
    UUID id, String name, Parcours parcours, UUID promotionId, String promotionName) {}
