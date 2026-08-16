package com.example.demo.dto.response;

import java.util.UUID;

public record StudentResponse(UUID id, String fullName, String email, String stdNumber) {}
