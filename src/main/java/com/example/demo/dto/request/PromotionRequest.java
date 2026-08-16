package com.example.demo.dto.request;

import lombok.Builder;

@Builder
public record PromotionRequest(String name, Integer year) {}
