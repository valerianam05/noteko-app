package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.AcademicYear;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.service.AcademicYearService;
import java.time.LocalDate;
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
class AcademicYearServiceTest {

  @Mock private AcademicYearRepository academicYearRepository;

  @InjectMocks private AcademicYearService academicYearService;

  private UUID yearId;
  private AcademicYear academicYear;

  @BeforeEach
  void setUp() {
    yearId = UUID.randomUUID();
    academicYear =
        AcademicYear.builder()
            .id(yearId)
            .label("2025-2026")
            .dateDebut(LocalDate.of(2025, 10, 1))
            .dateFin(LocalDate.of(2026, 7, 31))
            .isCurrent(true)
            .build();
  }

  @Test
  @DisplayName("findById doit retourner l'année académique si elle existe")
  void findById_Success() {
    when(academicYearRepository.findById(yearId)).thenReturn(Optional.of(academicYear));

    AcademicYear result = academicYearService.findById(yearId);

    assertNotNull(result);
    assertEquals("2025-2026", result.getLabel());
    assertTrue(result.isCurrent());
    verify(academicYearRepository, times(1)).findById(yearId);
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si l'année est introuvable")
  void findById_NotFound() {
    when(academicYearRepository.findById(yearId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> academicYearService.findById(yearId));
  }

  @Test
  @DisplayName("findAll doit retourner la liste de toutes les années académiques")
  void findAll_Success() {
    when(academicYearRepository.findAll()).thenReturn(List.of(academicYear));

    List<AcademicYear> results = academicYearService.findAll();

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(academicYearRepository, times(1)).findAll();
  }
}
