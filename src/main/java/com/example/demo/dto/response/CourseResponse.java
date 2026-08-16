package com.example.demo.dto.response;

import com.example.demo.entity.enums.Parcours;
import java.util.UUID;

public record CourseResponse(
    UUID id, String code, String name, Integer credits, UUID ueId, Parcours parcours) {}
