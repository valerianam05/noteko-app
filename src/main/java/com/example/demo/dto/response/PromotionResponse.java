package com.example.demo.dto.response;

import java.util.UUID;

public record PromotionResponse(
        UUID id,
        String name,
        Integer year
) {}