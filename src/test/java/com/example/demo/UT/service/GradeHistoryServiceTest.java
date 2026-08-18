package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Grade;
import com.example.demo.entity.GradeHistory;
import com.example.demo.repository.GradeHistoryRepository;
import com.example.demo.service.GradeHistoryService;
import java.time.OffsetDateTime;
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
class GradeHistoryServiceTest {

  @Mock private GradeHistoryRepository gradeHistoryRepository;

  @InjectMocks private GradeHistoryService gradeHistoryService;

  private UUID gradeId;
  private Grade grade;
  private GradeHistory gradeHistory;

  @BeforeEach
  void setUp() {
    gradeId = UUID.randomUUID();
    grade = Grade.builder().id(gradeId).score(14.5).build();

    gradeHistory =
        GradeHistory.builder()
            .id(UUID.randomUUID())
            .grade(grade)
            .oldValue(12.0)
            .newValue(14.5)
            .modifiedAt(OffsetDateTime.now())
            .build();
  }

  @Test
  @DisplayName("findByGradeId doit retourner l'historique trié par date descendante")
  void findByGradeId_Success() {
    when(gradeHistoryRepository.findByGrade_IdOrderByModifiedAtDesc(gradeId))
        .thenReturn(List.of(gradeHistory));

    List<GradeHistory> results = gradeHistoryService.findByGradeId(gradeId);

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(12.0, results.get(0).getOldValue());
    verify(gradeHistoryRepository, times(1)).findByGrade_IdOrderByModifiedAtDesc(gradeId);
  }

  @Test
  @DisplayName("save / logChange doit sauvegarder l'historique de modification")
  void save_Success() {
    when(gradeHistoryRepository.save(any(GradeHistory.class))).thenReturn(gradeHistory);

    GradeHistory saved = gradeHistoryRepository.save(gradeHistory);

    assertNotNull(saved);
    verify(gradeHistoryRepository, times(1)).save(any(GradeHistory.class));
  }
}
