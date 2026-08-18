package com.example.demo.dto.response;

import com.example.demo.entity.enums.AcademicLevel;
import com.example.demo.entity.enums.SemesterCode;
import java.util.UUID;

public record SemesterResponse(UUID id, SemesterCode code, AcademicLevel level, Integer orderNum) {}
