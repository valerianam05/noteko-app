package com.example.demo.dto.request;

import lombok.Builder;

@Builder
public record TeacherCreateRequest(
    String email, String password, String firstName, String lastName, String specialite) {}
