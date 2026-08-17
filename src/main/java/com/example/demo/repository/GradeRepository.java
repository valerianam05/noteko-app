package com.example.demo.repository;

import com.example.demo.entity.Grade;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, UUID> {

  List<Grade> findByStudentUserId(UUID studentUserId);

  List<Grade> findByEvaluationId(UUID evaluationId);

  List<Grade> findByStudentUserIdAndEvaluationId(UUID studentUserId, UUID evaluationId);

  List<Grade> findByPublished(boolean published);

  List<Grade> findByEvaluationIdAndPublished(UUID evaluationId, boolean published);

  Optional<Grade> findFirstByStudentUserIdAndEvaluationId(UUID studentUserId, UUID evaluationId);

  boolean existsByStudentUserIdAndEvaluationId(UUID studentUserId, UUID evaluationId);

  List<Grade> findAllByEvaluationIdAndPublishedFalse(UUID evaluationId);
}
