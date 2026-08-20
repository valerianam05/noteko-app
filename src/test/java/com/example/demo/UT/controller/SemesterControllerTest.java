package com.example.demo.UT.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.response.SemesterResponse;
import com.example.demo.endpoint.rest.controller.grade.SemesterController;
import com.example.demo.entity.Semester;
import com.example.demo.entity.enums.AcademicLevel;
import com.example.demo.entity.enums.SemesterCode;
import com.example.demo.mapper.SemesterMapper;
import com.example.demo.service.SemesterService;
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
class SemesterControllerTest {

  @Mock private SemesterService semesterService;

  @Mock private SemesterMapper semesterMapper;

  @InjectMocks private SemesterController semesterController;

  private UUID semesterId;
  private Semester semester;
  private SemesterResponse response;

  @BeforeEach
  void setUp() {
    semesterId = UUID.randomUUID();

    semester = Semester.builder().id(semesterId).code(SemesterCode.S1).orderNum(1).build();

    // SemesterResponse est probablement un record : impossible à mocker avec mock(...),
    // donc on construit une vraie instance.
    response = new SemesterResponse(semesterId, SemesterCode.S1, AcademicLevel.L1, 1);
  }

  @Test
  @DisplayName("list doit retourner les semestres triés par ordre")
  void list_Success() {
    when(semesterService.findAllOrdered()).thenReturn(List.of(semester));
    when(semesterMapper.toResponse(semester)).thenReturn(response);

    List<SemesterResponse> result = semesterController.list();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(response, result.get(0));

    verify(semesterService).findAllOrdered();
  }

  @Test
  @DisplayName("list doit retourner une liste vide si aucun semestre")
  void list_Empty() {
    when(semesterService.findAllOrdered()).thenReturn(List.of());

    List<SemesterResponse> result = semesterController.list();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
