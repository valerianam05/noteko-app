package com.example.demo.mapper;

import com.example.demo.dto.response.PromotionResponse;
import com.example.demo.entity.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionResponse toResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getYear());
    }
}