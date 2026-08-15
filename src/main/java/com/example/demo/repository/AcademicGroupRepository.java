package com.example.demo.repository;

import com.example.demo.entity.AcademicGroup;
import com.example.demo.entity.enums.Parcours;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicGroupRepository extends JpaRepository<AcademicGroup, UUID> {

  List<AcademicGroup> findByPromotionId(UUID promotionId);

  List<AcademicGroup> findByPromotionIdAndParcours(UUID promotionId, Parcours parcours);
}
