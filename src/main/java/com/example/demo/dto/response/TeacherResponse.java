package com.example.demo.dto.response;

import java.util.UUID;

public record TeacherResponse(UUID id, String fullName, String email, String specialite) {}
