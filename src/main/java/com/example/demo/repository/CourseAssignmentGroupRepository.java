package com.example.demo.repository;

import com.example.demo.entity.CourseAssignmentGroup;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAssignmentGroupRepository
    extends JpaRepository<CourseAssignmentGroup, UUID> {

  List<CourseAssignmentGroup> findByCourseAssignmentId(UUID courseAssignmentId);

  Optional<CourseAssignmentGroup> findByCourseAssignmentIdAndGroupId(
      UUID courseAssignmentId, UUID groupId);
}
