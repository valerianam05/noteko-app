package com.example.demo.dto.request;

import lombok.Builder;

@Builder
public record StudentCreateRequest(
    String email, String password, String firstName, String lastName, String stdNumber) {}
