package com.example.demo.service;

import com.example.demo.entity.CourseAssignment;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CourseAssignmentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseAssignmentService {
  private final CourseAssignmentRepository courseAssignmentRepository;

  @Transactional(readOnly = true)
  public CourseAssignment findById(UUID id) {
    return courseAssignmentRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Affectation de cours introuvable avec l'ID : " + id));
  }

  @Transactional(readOnly = true)
  public List<CourseAssignment> findByTeacher(UUID teacherUserId) {
    return courseAssignmentRepository.findByTeacherUserId(teacherUserId);
  }

  @Transactional(readOnly = true)
  public List<CourseAssignment> findBySubject(UUID subjectId) {
    return courseAssignmentRepository.findBySubjectId(subjectId);
  }
}
