package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.demo.entity.AcademicGroup;
import com.example.demo.entity.Promotion;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AcademicGroupRepository;
import com.example.demo.service.AcademicGroupService;
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
class AcademicGroupServiceTest {

  @Mock private AcademicGroupRepository academicGroupRepository;
  @Mock private PromotionService promotionService;

  @InjectMocks private AcademicGroupService academicGroupService;

  private UUID groupId;
  private UUID promotionId;
  private Promotion promotion;
  private AcademicGroup group;

  @BeforeEach
  void setUp() {
    groupId = UUID.randomUUID();
    promotionId = UUID.randomUUID();

    promotion = Promotion.builder().id(promotionId).name("L2 CS").build();

    group = AcademicGroup.builder().id(groupId).name("Groupe A").promotion(promotion).build();
  }

  @Test
  @DisplayName("findByPromotion doit retourner les groupes d'une promotion")
  void findByPromotion_Success() {
    when(academicGroupRepository.findByPromotionId(promotionId)).thenReturn(List.of(group));

    List<AcademicGroup> results = academicGroupService.findByPromotion(promotionId);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(academicGroupRepository, times(1)).findByPromotionId(promotionId);
  }

  @Test
  @DisplayName(
      "findByPromotionAndParcours doit retourner les groupes selon la promotion et le parcours")
  void findByPromotionAndParcours_Success() {
    when(academicGroupRepository.findByPromotionIdAndParcours(eq(promotionId), any(Parcours.class)))
        .thenReturn(List.of(group));

    Parcours targetParcours = Parcours.values()[0];
    List<AcademicGroup> results =
        academicGroupService.findByPromotionAndParcours(promotionId, targetParcours);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(academicGroupRepository, times(1))
        .findByPromotionIdAndParcours(eq(promotionId), any(Parcours.class));
  }

  @Test
  @DisplayName("findById doit retourner le groupe s'il existe")
  void findById_Success() {
    when(academicGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

    AcademicGroup result = academicGroupService.findById(groupId);

    assertNotNull(result);
    assertEquals("Groupe A", result.getName());
    verify(academicGroupRepository, times(1)).findById(groupId);
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si le groupe n'existe pas")
  void findById_NotFound() {
    when(academicGroupRepository.findById(groupId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> academicGroupService.findById(groupId));
    verify(academicGroupRepository, times(1)).findById(groupId);
  }

  @Test
  @DisplayName("create doit associer la promotion et sauvegarder le groupe")
  void create_Success() {
    Parcours targetParcours = Parcours.values()[0];
    when(promotionService.findById(promotionId)).thenReturn(promotion);
    when(academicGroupRepository.save(any(AcademicGroup.class))).thenReturn(group);

    AcademicGroup result = academicGroupService.create("Groupe A", targetParcours, promotionId);

    assertNotNull(result);
    assertEquals("Groupe A", result.getName());
    verify(promotionService, times(1)).findById(promotionId);
    verify(academicGroupRepository, times(1)).save(any(AcademicGroup.class));
  }
}
