package com.example.demo.repository;

import com.example.demo.entity.CourseAssignment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, UUID> {

  List<CourseAssignment> findByTeacherUserId(UUID teacherUserId);

  List<CourseAssignment> findBySubjectId(UUID subjectId);
}
