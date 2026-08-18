package com.example.demo.repository;

import com.example.demo.entity.CourseValidation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseValidationRepository extends JpaRepository<CourseValidation, UUID> {

  List<CourseValidation> findByStudent_UserId(UUID studentId);

  List<CourseValidation> findByStudent_UserIdAndAcademicYear_Id(
      UUID studentId, UUID academicYearId);

  Optional<CourseValidation> findByStudent_UserIdAndCourse_IdAndAcademicYear_Id(
      UUID studentId, UUID courseId, UUID academicYearId);

  @Query(
      """
      SELECT c.semester.level AS level, COALESCE(SUM(cv.creditsObtained), 0) AS totalCredits
      FROM CourseValidation cv
      JOIN cv.course c
      WHERE cv.student.userId = :studentId AND cv.validated = true
      GROUP BY c.semester.level
      """)
  List<Object[]> sumValidatedCreditsByLevel(@Param("studentId") UUID studentId);
}
