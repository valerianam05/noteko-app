package com.example.demo.service;

import com.example.demo.entity.GradeHistory;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GradeHistoryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeHistoryService {

  private final GradeHistoryRepository gradeHistoryRepository;

  @Transactional(readOnly = true)
  public List<GradeHistory> findByGradeId(UUID gradeId) {
    return gradeHistoryRepository.findByGrade_IdOrderByModifiedAtDesc(gradeId);
  }

  public GradeHistory save(GradeHistory history) {
    return gradeHistoryRepository.save(history);
  }

  @Transactional(readOnly = true)
  public GradeHistory findById(UUID id) {
    return gradeHistoryRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException("Historique de note introuvable avec l'ID : " + id));
  }
}
