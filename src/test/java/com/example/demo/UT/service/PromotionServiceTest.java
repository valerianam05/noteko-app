package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Promotion;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.service.PromotionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

  @Mock private PromotionRepository promotionRepository;

  @InjectMocks private PromotionService promotionService;

  private UUID promotionId;
  private Promotion promotion;

  @BeforeEach
  void setUp() {
    promotionId = UUID.randomUUID();
    promotion = Promotion.builder().id(promotionId).name("L2 CS").year(2026).build();
  }

  @Test
  @DisplayName("findAll doit retourner la liste des promotions triées par année descendante")
  void findAll_Success() {
    when(promotionRepository.findAllByOrderByYearDesc()).thenReturn(List.of(promotion));

    List<Promotion> results = promotionService.findAll();

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals("L2 CS", results.get(0).getName());
    verify(promotionRepository, times(1)).findAllByOrderByYearDesc();
  }

  @Test
  @DisplayName("findById doit retourner la promotion si elle existe")
  void findById_Success() {
    when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(promotion));

    Promotion result = promotionService.findById(promotionId);

    assertNotNull(result);
    assertEquals("L2 CS", result.getName());
    verify(promotionRepository, times(1)).findById(promotionId);
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si la promotion n'existe pas")
  void findById_NotFound() {
    when(promotionRepository.findById(promotionId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> promotionService.findById(promotionId));
    verify(promotionRepository, times(1)).findById(promotionId);
  }

  @Test
  @DisplayName("create doit créer et sauvegarder une nouvelle promotion")
  void create_Success() {
    when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

    Promotion result = promotionService.create("L2 CS", 2026);

    assertNotNull(result);
    assertEquals("L2 CS", result.getName());
    verify(promotionRepository, times(1)).save(any(Promotion.class));
  }
}
