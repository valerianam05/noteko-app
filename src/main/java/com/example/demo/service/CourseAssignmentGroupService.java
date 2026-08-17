package com.example.demo.service;

import com.example.demo.entity.CourseAssignmentGroup;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CourseAssignmentGroupRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseAssignmentGroupService {

  private final CourseAssignmentGroupRepository repository;

  @Transactional(readOnly = true)
  public List<CourseAssignmentGroup> findByCourseAssignmentId(UUID courseAssignmentId) {

    return repository.findByCourseAssignmentId(courseAssignmentId);
  }

  @Transactional(readOnly = true)
  public CourseAssignmentGroup findById(UUID id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Association cours-groupe introuvable avec l'ID : " + id));
  }

  public CourseAssignmentGroup save(CourseAssignmentGroup association) {
    return repository.save(association);
  }

  public void deleteByCourseAssignmentIdAndGroupId(UUID courseAssignmentId, UUID groupId) {

    CourseAssignmentGroup association =
        repository
            .findByCourseAssignmentIdAndGroupId(courseAssignmentId, groupId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Association entre l'affectation "
                            + courseAssignmentId
                            + " et le groupe "
                            + groupId
                            + " introuvable."));

    repository.delete(association);
  }
}
