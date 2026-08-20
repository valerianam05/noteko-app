package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.request.PromotionRequest;
import com.example.demo.dto.response.PromotionResponse;
import com.example.demo.endpoint.rest.controller.grade.PromotionController;
import com.example.demo.entity.Promotion;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.PromotionMapper;
import com.example.demo.service.PromotionService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {

  @Mock private PromotionService promotionService;

  @Mock private PromotionMapper promotionMapper;

  @InjectMocks private PromotionController promotionController;

  private UUID promotionId;
  private Promotion promotion;
  private PromotionResponse response;

  @BeforeEach
  void setUp() {
    promotionId = UUID.randomUUID();

    promotion = Promotion.builder().id(promotionId).name("Promo 2026").year(2026).build();

    // PromotionResponse est probablement un record (comme les autres Response de ce
    // projet) : impossible à mocker avec mock(...), donc on construit une vraie instance.
    response = new PromotionResponse(promotionId, "Promo 2026", 2026);
  }

  @Test
  @DisplayName("list doit retourner toutes les promotions")
  void list_Success() {
    when(promotionService.findAll()).thenReturn(List.of(promotion));
    when(promotionMapper.toResponse(promotion)).thenReturn(response);

    List<PromotionResponse> result = promotionController.list();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(response, result.get(0));

    verify(promotionService).findAll();
  }

  @Test
  @DisplayName("detail doit retourner la promotion demandée")
  void detail_Success() {
    when(promotionService.findById(promotionId)).thenReturn(promotion);
    when(promotionMapper.toResponse(promotion)).thenReturn(response);

    PromotionResponse result = promotionController.detail(promotionId);

    assertNotNull(result);
    assertEquals(response, result);

    verify(promotionService).findById(promotionId);
  }

  @Test
  @DisplayName("detail doit lever ResourceNotFoundException si la promotion n'existe pas")
  void detail_NotFound() {
    when(promotionService.findById(promotionId))
        .thenThrow(new ResourceNotFoundException("Promotion introuvable : " + promotionId));

    assertThrows(ResourceNotFoundException.class, () -> promotionController.detail(promotionId));
  }

  @Test
  @DisplayName("create doit créer une promotion")
  void create_Success() {
    PromotionRequest request = new PromotionRequest("Promo 2026", 2026);

    when(promotionService.create("Promo 2026", 2026)).thenReturn(promotion);
    when(promotionMapper.toResponse(promotion)).thenReturn(response);

    PromotionResponse result = promotionController.create(request);

    assertNotNull(result);
    assertEquals(response, result);

    verify(promotionService).create("Promo 2026", 2026);
  }
}
