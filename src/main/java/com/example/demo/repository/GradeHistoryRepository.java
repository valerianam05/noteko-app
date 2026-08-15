package com.example.demo.repository;

import com.example.demo.entity.GradeHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeHistoryRepository extends JpaRepository<GradeHistory, UUID> {
  List<GradeHistory> findByGrade_IdOrderByModifiedAtDesc(UUID gradeId);
}
