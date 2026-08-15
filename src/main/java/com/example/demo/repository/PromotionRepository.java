package com.example.demo.repository;

import com.example.demo.entity.Promotion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

  Optional<Promotion> findByYear(Integer year);

  List<Promotion> findAllByOrderByYearDesc();
}
