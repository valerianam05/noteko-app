package com.example.demo.service;

import com.example.demo.dto.request.CourseValidationRequest;
import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.Student;
import com.example.demo.mapper.CourseValidationMapper;
import com.example.demo.repository.CourseValidationRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseValidationService {

  private final CourseValidationRepository courseValidationRepository;
  private final StudentService studentService;
  private final CourseService courseService;
  private final AcademicYearService academicYearService;

  @Transactional(readOnly = true)
  public List<CourseValidationResponse> getCourseValidations(UUID studentId, UUID academicYearId) {
    List<CourseValidation> results;
    if (studentId != null && academicYearId != null) {
      results =
          courseValidationRepository.findByStudent_IdAndAcademicYear_Id(studentId, academicYearId);
    } else if (studentId != null) {
      results = courseValidationRepository.findByStudentUserId(studentId);
    } else {
      results = courseValidationRepository.findAll();
    }
    return results.stream().map(CourseValidationMapper::toResponse).toList();
  }

  public CourseValidationResponse computeCourseValidation(CourseValidationRequest request) {
    Student student = studentService.findById(request.studentId());
    Course course = courseService.findById(request.courseId());
    AcademicYear academicYear = academicYearService.findById(request.academicYearId());

    CourseValidation validation =
        courseValidationRepository
            .findByStudentUserIdAndCourseIdAndAcademicYearId(
                request.studentId(), request.courseId(), request.academicYearId())
            .orElse(new CourseValidation());

    validation.setStudent(student);
    validation.setCourse(course);
    validation.setAcademicYear(academicYear);
    validation.setFinalAverage(request.finalAverage());
    validation.setValidated(request.validated());
    validation.setCreditsObtained(request.creditsObtained());
    validation.setSession(request.session());
    validation.setComputedAt(OffsetDateTime.now());

    CourseValidation saved = courseValidationRepository.save(validation);
    return CourseValidationMapper.toResponse(saved);
  }
}
