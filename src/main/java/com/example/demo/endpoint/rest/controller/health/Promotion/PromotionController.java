package com.example.demo.endpoint.rest.controller.health.Promotion;

import com.example.demo.dto.request.PromotionRequest;
import com.example.demo.dto.response.PromotionResponse;
import com.example.demo.entity.Promotion;
import com.example.demo.mapper.PromotionMapper;
import com.example.demo.service.PromotionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

  private final PromotionService promotionService;
  private final PromotionMapper promotionMapper;

  @GetMapping
  public List<PromotionResponse> list() {
    return promotionService.findAll().stream().map(promotionMapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  public PromotionResponse detail(@PathVariable UUID id) {
    return promotionMapper.toResponse(promotionService.findById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PromotionResponse create(@Valid @RequestBody PromotionRequest request) {
    Promotion promotion = promotionService.create(request.name(), request.year());
    return promotionMapper.toResponse(promotion);
  }
}
