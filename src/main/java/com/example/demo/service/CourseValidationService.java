package com.example.demo.service;

import com.example.demo.dto.request.CourseValidationRequest;
import com.example.demo.dto.response.CourseValidationResponse;
import com.example.demo.entity.AcademicYear;
import com.example.demo.entity.Course;
import com.example.demo.entity.CourseValidation;
import com.example.demo.entity.Student;
import com.example.demo.entity.enums.SessionType;
import com.example.demo.mapper.CourseValidationMapper;
import com.example.demo.repository.CourseValidationRepository;
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
    List<CourseValidation> results = findValidationsByCriteria(studentId, academicYearId);
    return results.stream().map(CourseValidationMapper::toResponse).toList();
  }

  private List<CourseValidation> findValidationsByCriteria(UUID studentId, UUID academicYearId) {
    if (studentId != null && academicYearId != null) {
      return courseValidationRepository.findByStudent_UserIdAndAcademicYear_Id(
          studentId, academicYearId);
    }
    if (studentId != null) {
      return courseValidationRepository.findByStudent_UserId(studentId);
    }
    return courseValidationRepository.findAll();
  }

  public CourseValidationResponse computeCourseValidation(CourseValidationRequest request) {
    Student student = studentService.findById(request.studentId());
    Course course = courseService.findById(request.courseId());
    AcademicYear academicYear = academicYearService.findById(request.academicYearId());

    CourseValidation validation =
        courseValidationRepository
            .findByStudent_UserIdAndCourse_IdAndAcademicYear_Id(
                request.studentId(), request.courseId(), request.academicYearId())
            .orElseGet(CourseValidation::new);

    validation.setStudent(student);
    validation.setCourse(course);
    validation.setAcademicYear(academicYear);
    validation.setFinalAverage(request.finalAverage());
    validation.setValidated(request.validated());
    validation.setCreditsObtained(request.creditsObtained());
    validation.setSession(SessionType.valueOf(request.session()));

    CourseValidation saved = courseValidationRepository.save(validation);
    return CourseValidationMapper.toResponse(saved);
  }
}
