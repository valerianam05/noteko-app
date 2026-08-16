package com.example.demo.service;

import com.example.demo.entity.AcademicGroup;
import com.example.demo.entity.Promotion;
import com.example.demo.entity.enums.Parcours;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AcademicGroupRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicGroupService {

  private final AcademicGroupRepository academicGroupRepository;
  private final PromotionService promotionService;

  public List<AcademicGroup> findByPromotion(UUID promotionId) {
    return academicGroupRepository.findByPromotionId(promotionId);
  }

  public List<AcademicGroup> findByPromotionAndParcours(UUID promotionId, Parcours parcours) {
    return academicGroupRepository.findByPromotionIdAndParcours(promotionId, parcours);
  }

  public AcademicGroup findById(UUID id) {
    return academicGroupRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Groupe introuvable : " + id));
  }

  public AcademicGroup create(String name, Parcours parcours, UUID promotionId) {
    Promotion promotion = promotionService.findById(promotionId);
    AcademicGroup group =
        AcademicGroup.builder().name(name).parcours(parcours).promotion(promotion).build();
    return academicGroupRepository.save(group);
  }
}
