package com.example.demo.UT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.entity.CourseAssignmentGroup;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CourseAssignmentGroupRepository;
import com.example.demo.service.CourseAssignmentGroupService;
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
class CourseAssignmentGroupServiceTest {

  @Mock private CourseAssignmentGroupRepository repository;

  @InjectMocks private CourseAssignmentGroupService courseAssignmentGroupService;

  private UUID associationId;
  private UUID courseAssignmentId;
  private UUID groupId;
  private CourseAssignmentGroup association;

  @BeforeEach
  void setUp() {
    associationId = UUID.randomUUID();
    courseAssignmentId = UUID.randomUUID();
    groupId = UUID.randomUUID();

    association = CourseAssignmentGroup.builder().id(associationId).build();
  }

  @Test
  @DisplayName("findByCourseAssignmentId doit retourner la liste des associations pour un cours")
  void findByCourseAssignmentId_Success() {
    when(repository.findByCourseAssignmentId(courseAssignmentId)).thenReturn(List.of(association));

    List<CourseAssignmentGroup> results =
        courseAssignmentGroupService.findByCourseAssignmentId(courseAssignmentId);

    assertNotNull(results);
    assertEquals(1, results.size());
    verify(repository, times(1)).findByCourseAssignmentId(courseAssignmentId);
  }

  @Test
  @DisplayName("findById doit retourner l'association quand elle existe")
  void findById_Success() {
    when(repository.findById(associationId)).thenReturn(Optional.of(association));

    CourseAssignmentGroup result = courseAssignmentGroupService.findById(associationId);

    assertNotNull(result);
    assertEquals(associationId, result.getId());
    verify(repository, times(1)).findById(associationId);
  }

  @Test
  @DisplayName("findById doit lever ResourceNotFoundException si l'association n'existe pas")
  void findById_NotFound() {
    when(repository.findById(associationId)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> courseAssignmentGroupService.findById(associationId));
    verify(repository, times(1)).findById(associationId);
  }

  @Test
  @DisplayName("save doit enregistrer et retourner l'association")
  void save_Success() {
    when(repository.save(any(CourseAssignmentGroup.class))).thenReturn(association);

    CourseAssignmentGroup result = courseAssignmentGroupService.save(association);

    assertNotNull(result);
    verify(repository, times(1)).save(association);
  }

  @Test
  @DisplayName("deleteByCourseAssignmentIdAndGroupId doit supprimer l'association existante")
  void deleteByCourseAssignmentIdAndGroupId_Success() {
    when(repository.findByCourseAssignmentIdAndGroupId(courseAssignmentId, groupId))
        .thenReturn(Optional.of(association));

    courseAssignmentGroupService.deleteByCourseAssignmentIdAndGroupId(courseAssignmentId, groupId);

    verify(repository, times(1)).findByCourseAssignmentIdAndGroupId(courseAssignmentId, groupId);
    verify(repository, times(1)).delete(association);
  }

  @Test
  @DisplayName(
      "deleteByCourseAssignmentIdAndGroupId doit lever ResourceNotFoundException si l'association"
          + " n'existe pas")
  void deleteByCourseAssignmentIdAndGroupId_NotFound() {
    when(repository.findByCourseAssignmentIdAndGroupId(courseAssignmentId, groupId))
        .thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () ->
            courseAssignmentGroupService.deleteByCourseAssignmentIdAndGroupId(
                courseAssignmentId, groupId));

    verify(repository, times(1)).findByCourseAssignmentIdAndGroupId(courseAssignmentId, groupId);
    verify(repository, never()).delete(any());
  }
}
