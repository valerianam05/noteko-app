package com.example.demo.dto.response;

import com.example.demo.entity.enums.Parcours;
import java.util.UUID;

public record CourseResponse(
    UUID id, String code, String title, Integer credits, Parcours parcours, UUID semesterId) {}
