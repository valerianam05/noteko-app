package com.example.demo.repository;

import com.example.demo.entity.StudentEnrollment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, UUID> {

  Optional<StudentEnrollment> findByStudentIdAndAcademicYearId(UUID studentId, UUID academicYearId);

  List<StudentEnrollment> findByGroupIdAndAcademicYearId(UUID groupId, UUID academicYearId);

  List<StudentEnrollment> findByStudentIdOrderByAcademicYearIdAsc(UUID studentId);
}
