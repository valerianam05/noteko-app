package com.example.demo.service;

import com.example.demo.entity.Promotion;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PromotionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionService {

  private final PromotionRepository promotionRepository;

  public List<Promotion> findAll() {
    return promotionRepository.findAllByOrderByYearDesc();
  }

  public Promotion findById(UUID id) {
    return promotionRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Promotion introuvable : " + id));
  }

  public Promotion create(String name, Integer year) {
    Promotion promotion = Promotion.builder().name(name).year(year).build();
    return promotionRepository.save(promotion);
  }
}
